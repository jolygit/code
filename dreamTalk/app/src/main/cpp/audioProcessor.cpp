#include "audioProcessor.h"
#include <unistd.h>
#include <deque>
#define RECORDER_FRAMES (700)
static bool start=true;
std::deque<short*> voiceBufferDeque;
static const int BufCount=30;
static short voiceBuffer[BufCount][RECORDER_FRAMES];
static short* voiceBufferPtrs[BufCount];
static bool  voiceBufferAvailable[BufCount];
//static char recording[4096*536];
static short recorderBuffer[RECORDER_FRAMES+2];//2 is for packet indexing
static short recorderBuffer1[RECORDER_FRAMES+2];
static short bufEmpty[RECORDER_FRAMES];
static const SLEnvironmentalReverbSettings reverbSettings=SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
// engine interfaces
static SLObjectItf engineObject = NULL;
static SLEngineItf engineEngine;
// output mix interfaces
static SLObjectItf outputMixObject = NULL;
static SLEnvironmentalReverbItf outputMixEnvironmentalReverb = NULL;
// buffer queue player interfaces
static SLObjectItf bqPlayerObject = NULL;
static SLPlayItf bqPlayerPlay;
static SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue;
static SLEffectSendItf bqPlayerEffectSend;
static SLMuteSoloItf bqPlayerMuteSolo;
static SLVolumeItf bqPlayerVolume;
static SLmilliHertz bqPlayerSampleRate = 0;
static jint   bqPlayerBufSize = 0;

// recorder interfaces
static SLObjectItf recorderObject = NULL;
static SLRecordItf recorderRecord;
static SLAndroidSimpleBufferQueueItf recorderBufferQueue;
static unsigned recorderSize;
// pointer and size of the next player buffer to enqueue, and number of remaining buffers
static const int sampleRate=8000;
static const int bufSize=RECORDER_FRAMES;
static int rcnt=0;
static int packetCnt=0;
static struct sockaddr_in peerAddress;
static int peerSocketFd;
static int Threshold=1;

bool AudioProcessor::SetUp(){
    start=true;
    createEngine();
    createBufferQueueAudioPlayer(sampleRate, bufSize);
    createAudioRecorder();
    for(int i=0;i<BufCount;i++){
        voiceBufferPtrs[i]=voiceBuffer[i];
        voiceBufferAvailable[i]=true;
    }
    for(int i=0;i<RECORDER_FRAMES;i++)
        bufEmpty[i]=0;
    return true;
}

// this callback handler is called every time a buffer finishes playing
void AudioProcessor::bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context)
{
    assert(bq == bqPlayerBufferQueue);
    assert(NULL == context);
    if(voiceBufferDeque.size()>Threshold) {
        short* buf=voiceBufferDeque.front();
        Threshold=1;
        voiceBufferDeque.pop_front();
        for (int i = 0; i < BufCount; i++)
            if (voiceBufferPtrs[i] == buf)//make the poped buffer available again
                voiceBufferAvailable[i] = true;
        SLresult result;
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, buf,
                                                 RECORDER_FRAMES * sizeof(short));
    }
    else {//when we deplete packets we play empty packet for fove packet times
        Threshold=5;
        SLresult result;
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, bufEmpty,
                                                 RECORDER_FRAMES * sizeof(short));
    }
}


// this callback handler is called every time a buffer finishes recording we supplied two buffers recorderBuffer and recorderBuffer1 when either gets full we send its contents to peer and then add it back to the queue
void AudioProcessor::bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context)
{
    assert(bq == recorderBufferQueue);
    assert(NULL == context);
    SLresult result;
    if(rcnt==0){
        rcnt=1;
        result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer+2,RECORDER_FRAMES * sizeof(short));
        result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
        int* r=(int*)recorderBuffer;
        r[0]=packetCnt;//indexing all the packets to avoid duplicate packet playback
        sendto(peerSocketFd,(void*)recorderBuffer,(RECORDER_FRAMES+2) * sizeof(short),0,(SA *) &peerAddress,sizeof(peerAddress));
    }
    else if(rcnt==1){
        rcnt=0;
        result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer1+2,RECORDER_FRAMES * sizeof(short));
        result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
        int* r=(int*)recorderBuffer1;//indexing all the packets to avoid duplicate packet playback
        r[0]=packetCnt;
        sendto(peerSocketFd,(void*)recorderBuffer1,(RECORDER_FRAMES+2) * sizeof(short),0,(SA *) &peerAddress,sizeof(peerAddress));
    }
    packetCnt++;
}


// create the engine and output mix objects
void AudioProcessor::createEngine()
{
    SLresult result;

    // create engine
    result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // realize the engine
    result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the engine interface, which is needed in order to create other objects
    result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // create output mix, with environmental reverb specified as a non-required interface
    const SLInterfaceID ids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean req[1] = {SL_BOOLEAN_FALSE};
    result = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 1, ids, req);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // realize the output mix
    result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the environmental reverb interface
    // this could fail if the environmental reverb effect is not available,
    // either because the feature is not present, excessive CPU load, or
    // the required MODIFY_AUDIO_SETTINGS permission was not requested and granted
    result = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB,
                                              &outputMixEnvironmentalReverb);
    if (SL_RESULT_SUCCESS == result) {
        result = (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(
                outputMixEnvironmentalReverb, &reverbSettings);
        (void)result;
    }
}

// create buffer queue audio player
void AudioProcessor::createBufferQueueAudioPlayer(int sampleRate,int bufSize)
{
    SLresult result;
    if (sampleRate >= 0 && bufSize >= 0 ) {
        bqPlayerSampleRate = sampleRate * 1000;
        /*
         * device native buffer size is another factor to minimize audio latency, not used in this
         * sample: we only play one giant buffer here
         */
        bqPlayerBufSize = bufSize;
    }

    // configure audio source
    SLDataLocator_AndroidSimpleBufferQueue loc_bufq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM, 1, SL_SAMPLINGRATE_8,
                                   SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
                                   SL_SPEAKER_FRONT_CENTER, SL_BYTEORDER_LITTLEENDIAN};//SL_SPEAKER_TOP_FRONT_LEFT|SL_SPEAKER_TOP_FRONT_CENTER|SL_SPEAKER_TOP_FRONT_RIGHT|SL_SPEAKER_FRONT_CENTER SL_SPEAKER_FRONT_LEFT
    /*
     * Enable Fast Audio when possible:  once we set the same rate to be the native, fast audio path
     * will be triggered
     */
    if(bqPlayerSampleRate) {
        format_pcm.samplesPerSec = bqPlayerSampleRate;       //sample rate in mili second
    }
    SLDataSource audioSrc = {&loc_bufq, &format_pcm};

    // configure audio sink
    SLDataLocator_OutputMix loc_outmix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
    SLDataSink audioSnk = {&loc_outmix, NULL};


    const SLInterfaceID ids[2] = {SL_IID_ANDROIDCONFIGURATION,SL_IID_BUFFERQUEUE};//,SL_IID_VOLUME, SL_IID_EFFECTSEND};//, /*,            SL_IID_MUTESOLO,*/};
    const SLboolean req[2] = {SL_BOOLEAN_TRUE,SL_BOOLEAN_TRUE};//,SL_BOOLEAN_TRUE,SL_BOOLEAN_TRUE};

    result = (*engineEngine)->CreateAudioPlayer(engineEngine, &bqPlayerObject, &audioSrc, &audioSnk,
                                                2, ids, req);//bqPlayerSampleRate? 2 : 3
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    //seting the ear speaker
    SLAndroidConfigurationItf playerConfig;
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_ANDROIDCONFIGURATION, &playerConfig);
    assert(SL_RESULT_SUCCESS == result);
    SLint32 streamType = SL_ANDROID_STREAM_VOICE;
    (void)result;
    result = (*playerConfig)->SetConfiguration(playerConfig, SL_ANDROID_KEY_STREAM_TYPE, &streamType, sizeof(SLint32));
    assert(SL_RESULT_SUCCESS == result);
    // realize the player
    result = (*bqPlayerObject)->Realize(bqPlayerObject, SL_BOOLEAN_FALSE);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the play interface
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_PLAY, &bqPlayerPlay);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the buffer queue interface
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_BUFFERQUEUE,
                                             &bqPlayerBufferQueue);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // register callback on the buffer queue
    result = (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue, bqPlayerCallback, NULL);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the effect send interface
    /*bqPlayerEffectSend = NULL;
    if( 0 == bqPlayerSampleRate) {
        result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_EFFECTSEND,
                                                 &bqPlayerEffectSend);
        assert(SL_RESULT_SUCCESS == result);
        (void)result;
    }

    // get the volume interface
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_VOLUME, &bqPlayerVolume);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;*/

    // set the player's state to playing
    result = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
}
bool AudioProcessor::Play(short* nextBufferToPlay,int nextBufferLength){
    int i;
    for (i=0;i<BufCount;i++){
        if(voiceBufferAvailable[i])
            break;
    }
    if(i>=BufCount)
        return false;
    for (int j=0;j<RECORDER_FRAMES;j++)
        voiceBuffer[i][j]=nextBufferToPlay[j];
    voiceBufferDeque.push_back(voiceBufferPtrs[i]);
    voiceBufferAvailable[i]=false;
    SLresult result;
    if(start && i==20) {//only the first time we add buffer in here, subsequanty all the additions will be in bqPlayerCallback function above
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, voiceBufferPtrs[0],RECORDER_FRAMES* sizeof(short));
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, voiceBufferPtrs[1],RECORDER_FRAMES* sizeof(short));
        voiceBufferDeque.pop_front();
        voiceBufferDeque.pop_front();
        voiceBufferAvailable[0]=true;
        voiceBufferAvailable[1]=true;
        start=false;
    }
    return true;
}
// create audio recorder: recorder is not in fast path
//    like to avoid excessive re-sampling while playing back from Hello & Android clip
bool AudioProcessor::createAudioRecorder()
{
    SLresult result;

    // configure audio source
    SLDataLocator_IODevice loc_dev = {SL_DATALOCATOR_IODEVICE, SL_IODEVICE_AUDIOINPUT,
                                      SL_DEFAULTDEVICEID_AUDIOINPUT, NULL};
    SLDataSource audioSrc = {&loc_dev, NULL};

    // configure audio sink
    SLDataLocator_AndroidSimpleBufferQueue loc_bq = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
    SLDataFormat_PCM format_pcm = {SL_DATAFORMAT_PCM, 1, SL_SAMPLINGRATE_8,
                                   SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
                                   SL_SPEAKER_FRONT_CENTER, SL_BYTEORDER_LITTLEENDIAN};
    SLDataSink audioSnk = {&loc_bq, &format_pcm};

    // create audio recorder
    // (requires the RECORD_AUDIO permission)
    const SLInterfaceID id[1] = {SL_IID_ANDROIDSIMPLEBUFFERQUEUE};
    const SLboolean req[1] = {SL_BOOLEAN_TRUE};
    result = (*engineEngine)->CreateAudioRecorder(engineEngine, &recorderObject, &audioSrc,
                                                  &audioSnk, 1, id, req);
    if (SL_RESULT_SUCCESS != result) {
        return JNI_FALSE;
    }

    // realize the audio recorder
    result = (*recorderObject)->Realize(recorderObject, SL_BOOLEAN_FALSE);
    if (SL_RESULT_SUCCESS != result) {
        return JNI_FALSE;
    }

    // get the record interface
    result = (*recorderObject)->GetInterface(recorderObject, SL_IID_RECORD, &recorderRecord);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // get the buffer queue interface
    result = (*recorderObject)->GetInterface(recorderObject, SL_IID_ANDROIDSIMPLEBUFFERQUEUE,
                                             &recorderBufferQueue);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // register callback on the buffer queue
    result = (*recorderBufferQueue)->RegisterCallback(recorderBufferQueue, bqRecorderCallback,
                                                      NULL);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    return JNI_TRUE;
}


// set the recording state for the audio recorder
void AudioProcessor::startRecording(int sfd,struct sockaddr_in fraddress)
{
    //this function is called only once and we get the address and socket to send the voice packets to here.
    peerAddress=fraddress;
    peerSocketFd=sfd;
    SLresult result;
    // in case already recording, stop recording and clear buffer queue
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    result = (*recorderBufferQueue)->Clear(recorderBufferQueue);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    // the buffer is not valid for playback yet
    recorderSize = 0;
    // enqueue an empty buffer to be filled by the recorder
    // (for streaming recording, we would enqueue at least 2 empty buffers to start things off)
    result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer,
                                             RECORDER_FRAMES * sizeof(short));
    // the most likely other result is SL_RESULT_BUFFER_INSUFFICIENT,
    // which for this code example would indicate a programming error
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer1,
                                             RECORDER_FRAMES * sizeof(short));
    // the most likely other result is SL_RESULT_BUFFER_INSUFFICIENT,
    // which for this code example would indicate a programming error
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    // start recording
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
}
void AudioProcessor::stopRecording() {
    SLresult result;
    result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
    result = (*recorderBufferQueue)->Clear(recorderBufferQueue);
    assert(SL_RESULT_SUCCESS == result);

    ///stop player
    result = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_STOPPED);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
}
// shut down the native audio system
void AudioProcessor::shutdown()
{

    // destroy buffer queue audio player object, and invalidate all associated interfaces
    if (bqPlayerObject != NULL) {
        (*bqPlayerObject)->Destroy(bqPlayerObject);
        bqPlayerObject = NULL;
        bqPlayerPlay = NULL;
        bqPlayerBufferQueue = NULL;
        bqPlayerEffectSend = NULL;
        bqPlayerMuteSolo = NULL;
        bqPlayerVolume = NULL;
    }



    // destroy audio recorder object, and invalidate all associated interfaces
    if (recorderObject != NULL) {
        (*recorderObject)->Destroy(recorderObject);
        recorderObject = NULL;
        recorderRecord = NULL;
        recorderBufferQueue = NULL;
    }

    // destroy output mix object, and invalidate all associated interfaces
    if (outputMixObject != NULL) {
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject = NULL;
        outputMixEnvironmentalReverb = NULL;
    }

    // destroy engine object, and invalidate all associated interfaces
    if (engineObject != NULL) {
        (*engineObject)->Destroy(engineObject);
        engineObject = NULL;
        engineEngine = NULL;
    }
}

