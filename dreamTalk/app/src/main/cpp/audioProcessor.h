#include	<sys/socket.h>	/* basic socket definitions */
#include	<sys/time.h>	/* timeval{} for select() */
#include	<time.h>		/* timespec{} for pselect() */
#include	<netinet/in.h>	/* sockaddr_in{} and other Internet defns */
#include	<arpa/inet.h>	/* inet(3) functions */
#include	<errno.h>
#include	<fcntl.h>		/* for nonblocking */
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
#define	SA	struct sockaddr
#include  <sstream>
#include  <string>
#include <unistd.h>
using namespace std;

class AudioProcessor{
public:
    AudioProcessor(){
    }
    bool SetUp();
    void shutdown();
    bool SetUpPlayer();
    bool SetUpRecorder();
    bool Record();
    bool Play(short* nextBuffer,int nextSize);
    //bool PlaySoundPacket(const char* buf);
    void startRecording(int sfd,struct sockaddr_in fraddress);
    void stopRecording();
private:
    static void releaseResampleBuf(void);
    short* createResampledBuf(uint32_t idx, uint32_t srcRate, unsigned *size);
    static void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context);
    static void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context);
    void createEngine();
    void createBufferQueueAudioPlayer(int sampleRate, int bufSize);
    bool selectClip();
    bool createAudioRecorder();
};
