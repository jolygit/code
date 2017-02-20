#include "audioProcessor.h"
#include <unistd.h>
bool AudioProcessor::SetUp(){
  createEngine();
  createBufferQueueAudioPlayer(sampleRate, bufSize);
  createAudioRecorder();
  return true;
}
bool AudioProcessor::Record(){
  startRecording();
  return true;
}
bool AudioProcessor::Play(){
  sleep(16);
  selectClip();
    return true;
}
void AudioProcessor::releaseResampleBuf(void) {
    if( 0 == bqPlayerSampleRate) {
        /*
         * we are not using fast path, so we were not creating buffers, nothing to do
         */
        return;
    }

    free(resampleBuf);
    resampleBuf = NULL;
}

/*
 * Only support up-sampling
 */
short* AudioProcessor::createResampledBuf(uint32_t idx, uint32_t srcRate, unsigned *size) {
  *size = sizeof(recording);
  resampleBuf=recording;
  return resampleBuf;
}

// this callback handler is called every time a buffer finishes playing
void AudioProcessor::bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context)
{
    assert(bq == bqPlayerBufferQueue);
    assert(NULL == context);
    // for streaming playback, replace this test by logic to find and fill the next buffer
    if (--nextCount > 0 && NULL != nextBuffer && 0 != nextSize) {
        SLresult result;
        // enqueue another buffer
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, nextBuffer, nextSize);
        // the most likely other result is SL_RESULT_BUFFER_INSUFFICIENT,
        // which for this code example would indicate a programming error
        if (SL_RESULT_SUCCESS != result) {
	  //pthread_mutex_unlock(&audioEngineLock);
        }
        (void)result;
    } else {
        releaseResampleBuf();
        //pthread_mutex_unlock(&audioEngineLock);
    }
}


// this callback handler is called every time a buffer finishes recording
void AudioProcessor::bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context)
{
    assert(bq == recorderBufferQueue);
    assert(NULL == context);
    SLresult result;
    // for streaming recording, here we would call Enqueue to give recorder the next buffer to fill
    // but instead, this is a one-time buffer so we stop recording
    if(rcnt==0){
      for(int i=0;i<RECORDER_FRAMES;i++){
	recording[rcnt*RECORDER_FRAMES+i]=recorderBuffer[i];
      }
      rcnt++;
      result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer,RECORDER_FRAMES * sizeof(short));
      result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
      //result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
      //recorderSize = 2*RECORDER_FRAMES * sizeof(short);
    }
     else if(rcnt==1){
       for(int i=0;i<RECORDER_FRAMES;i++){
     	recording[rcnt*RECORDER_FRAMES+i]=recorderBuffer1[i];
       }
       rcnt++;
        result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
     }
    else {
       for(int i=0;i<RECORDER_FRAMES;i++){
       	recording[rcnt*RECORDER_FRAMES+i]=recorderBuffer[i];
       }
      rcnt=0;
      result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_STOPPED);
      recorderSize = 3*RECORDER_FRAMES * sizeof(short);
    }
    // pthread_mutex_unlock(&audioEngineLock);
    //replace the above code with where tosendBuff is that you will be sending
    // if(rcnt%2==0){
    //   rcnt++;
    //   for(int i=0;i<RECORDER_FRAMES;i++){
    // 	tosendBuff[i]=recorderBuffer[i];
    //   }
    //   result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer,RECORDER_FRAMES * sizeof(short));
    //   result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
    // }
    // else{
    //   rcnt++;
    //   for(int i=0;i<RECORDER_FRAMES;i++){
    // 	tosendBuff[i]=recorderBuffer1[i];
    //   }
    //   result = (*recorderBufferQueue)->Enqueue(recorderBufferQueue, recorderBuffer1,RECORDER_FRAMES * sizeof(short));
    //   result = (*recorderRecord)->SetRecordState(recorderRecord, SL_RECORDSTATE_RECORDING);
    // }
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
    // ignore unsuccessful result codes for environmental reverb, as it is optional for this example

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
        SL_SPEAKER_FRONT_CENTER, SL_BYTEORDER_LITTLEENDIAN};
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

   
    const SLInterfaceID ids[3] = {SL_IID_BUFFERQUEUE, SL_IID_VOLUME, SL_IID_EFFECTSEND,
                                    /*SL_IID_MUTESOLO,*/};
    const SLboolean req[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE,
                                   /*SL_BOOLEAN_TRUE,*/ };

    result = (*engineEngine)->CreateAudioPlayer(engineEngine, &bqPlayerObject, &audioSrc, &audioSnk,
            bqPlayerSampleRate? 2 : 3, ids, req);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

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
    bqPlayerEffectSend = NULL;
    if( 0 == bqPlayerSampleRate) {
        result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_EFFECTSEND,
                                                 &bqPlayerEffectSend);
        assert(SL_RESULT_SUCCESS == result);
        (void)result;
    }

    // get the volume interface
    result = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_VOLUME, &bqPlayerVolume);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;

    // set the player's state to playing
    result = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);
    assert(SL_RESULT_SUCCESS == result);
    (void)result;
}

// enable reverb on the buffer queue player
// bool AudioProcessor::enableReverb(bool enabled)
// {
//     SLresult result;

//     // we might not have been able to add environmental reverb to the output mix
//     if (NULL == outputMixEnvironmentalReverb) {
//         return JNI_FALSE;
//     }

//     if(bqPlayerSampleRate) {
       
//         return JNI_FALSE;
//     }
//     result = (*bqPlayerEffectSend)->EnableEffectSend(bqPlayerEffectSend,
//             outputMixEnvironmentalReverb, (SLboolean) enabled, (SLmillibel) 0);
//     // and even if environmental reverb was present, it might no longer be available
//     if (SL_RESULT_SUCCESS != result) {
//         return JNI_FALSE;
//     }

//     return JNI_TRUE;
// }

// select the desired clip and play count, and enqueue the first buffer if idle
bool AudioProcessor::selectClip()
{
   // if (pthread_mutex_trylock(&audioEngineLock)) {
        // If we could not acquire audio engine lock, reject this request and client should re-try
    //    return JNI_FALSE;
    //}
    nextBuffer = createResampledBuf(4, SL_SAMPLINGRATE_8, &nextSize);
    nextCount = 1;
    if (nextSize > 0) {
        // here we only enqueue one buffer because it is a long clip,
        // but for streaming playback we would typically enqueue at least 2 buffers to start
        SLresult result;
        result = (*bqPlayerBufferQueue)->Enqueue(bqPlayerBufferQueue, nextBuffer, nextSize);
       // if (SL_RESULT_SUCCESS != result) {
       //     pthread_mutex_unlock(&audioEngineLock);
       //     return JNI_FALSE;
       // }
    }
    return JNI_TRUE;
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
void AudioProcessor::startRecording()
{
    SLresult result;

    // if (pthread_mutex_trylock(&audioEngineLock)) {
    //     return;
    // }
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

    // pthread_mutex_destroy(&audioEngineLock);
}
extern "C" {
JNIEXPORT jboolean JNICALL
Java_com_google_sample_echo_MainActivity_SetUp(JNIEnv *env, jclass type);
}
JNIEXPORT jboolean JNICALL
Java_com_google_sample_echo_MainActivity_SetUp(JNIEnv *env, jclass type) {
  AudioProcessor ap;
  ap.SetUp();
  ap.Record();
  ap.Play();
    return JNI_TRUE;
}
