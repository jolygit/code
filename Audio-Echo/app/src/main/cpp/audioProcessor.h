#include <assert.h>
#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <vector>
// for native audio
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

// for native asset manager
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#ifndef AUDIO_ECHO_AUDIOPROCESSOR_H
#define AUDIO_ECHO_AUDIOPROCESSOR_H

#endif //AUDIO_ECHO_AUDIOPROCESSOR_H
#define RECORDER_FRAMES (8000 * 5)
#include  <sstream>
#include  <string>
#include <unistd.h>
using namespace std;
static short recording[3*RECORDER_FRAMES];
static short recorderBuffer[RECORDER_FRAMES];
static short recorderBuffer1[RECORDER_FRAMES];
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
static short *resampleBuf = NULL;
// a mutext to guard against re-entrance to record & playback
// as well as make recording and playing back to be mutually exclusive
// this is to avoid crash at situations like:
//    recording is in session [not finished]
//    user presses record button and another recording coming in
// The action: when recording/playing back is not finished, ignore the new request
static pthread_mutex_t  audioEngineLock = PTHREAD_MUTEX_INITIALIZER;



// recorder interfaces
static SLObjectItf recorderObject = NULL;
static SLRecordItf recorderRecord;
static SLAndroidSimpleBufferQueueItf recorderBufferQueue;

// aux effect on the output mix, used by the buffer queue player



static unsigned recorderSize;

// pointer and size of the next player buffer to enqueue, and number of remaining buffers
static short *nextBuffer;
static unsigned nextSize;
static int nextCount;
static const int sampleRate=8000;
static const int bufSize=240;
static int rcnt=0;
class AudioProcessor{
 public:
  AudioProcessor(){
    /* 
     * engineObject = NULL;
     * outputMixObject = NULL;
     * outputMixEnvironmentalReverb = NULL;
     * bqPlayerObject = NULL;
     * resampleBuf = NULL;
     * bqPlayerSampleRate = 0;
     * bqPlayerBufSize = 0;
     * audioEngineLock = PTHREAD_MUTEX_INITIALIZER;
     * recorderObject = NULL;
     * recorderSize = 0;
     */
    //sampleRate=8000;
    //bufSize=240;
    //rcnt=0;
  }
  bool SetUp();
  bool SetUpPlayer();
  bool SetUpRecorder();
  bool Record();
  //bool RecordAndSend(int sfd,struct sockaddr_in fraddress);
  bool Play();
  //bool PlaySoundPacket(const char* buf);
 private:

static void releaseResampleBuf(void);
short* createResampledBuf(uint32_t idx, uint32_t srcRate, unsigned *size);
static void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context);
static void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context);
void createEngine();
void createBufferQueueAudioPlayer(int sampleRate, int bufSize);
bool selectClip();
bool createAudioRecorder();
void startRecording();
void shutdown();


// 5 seconds of recorded audio at 16 kHz mono, 16-bit signed little endian

};
