#include <alsa/asoundlib.h>
#include "unp.h"//sp
#define ALSA_PCM_NEW_HW_PARAMS_API
class SoundProcessor{
 public:
  SoundProcessor(){
      framecnt=0;
  }
  bool SetUpPlayer();
  bool SetUpRecorder();
  bool Record();
  bool RecordAndSend(int sfd,struct sockaddr_in fraddress);
  bool Play();
  bool PlaySoundPacket(char* buf);
 private:
  int size;
  /* Set period size to 64 frames. */
  snd_pcm_uframes_t frames=64;
  // s stands for speaker
  long loops;
  int rc_s;
  snd_pcm_t *handle_s;
  snd_pcm_hw_params_t *params_s;
  unsigned int val_s;
  int dir_s;
  char *buffer_s;
  // m stands for microphone
  int rc_m;
  snd_pcm_t *handle_m;
  snd_pcm_hw_params_t *params_m;
  unsigned int val_m;
  int dir_m;
  char *buffer_m;
  int  framecnt;
};
