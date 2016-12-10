#include "soundProcessor.h"

bool SoundProcessor::Play(){
  while (true) {
    rc_s = read(0, buffer_s, size);
    if (rc_s == 0) {
      fprintf(stderr, "end of file on input\n");
      break;
    } else if (rc_s != size) {
      fprintf(stderr,
              "short read: read %d bytes\n", rc_s);
    }
    rc_s = snd_pcm_writei(handle_s, buffer_s, frames);
    if (rc_s == -EPIPE) {
      /* EPIPE means underrun */
      // fprintf(stderr, "underrun occurred\n");
      snd_pcm_prepare(handle_s);
    } else if (rc_s < 0) {
      fprintf(stderr,
              "error from writei: %s\n",
              snd_strerror(rc_s));
    }  else if (rc_s != (int)frames) {
      fprintf(stderr,
              "short write, write %d frames\n", rc_s);
    }
  }

  snd_pcm_drain(handle_s);
  snd_pcm_close(handle_s);
  free(buffer_s);
  return true;
}
bool SoundProcessor::PlaySoundPacket(const char* buf){
  rc_s = snd_pcm_writei(handle_s, buf, frames);
    if (rc_s == -EPIPE) {
      /* EPIPE means underrun */
      fprintf(stderr, "underrun occurred\n");
      snd_pcm_prepare(handle_s);
    } else if (rc_s < 0) {
      fprintf(stderr,
              "error from writei: %s\n",
              snd_strerror(rc_s));
    }  else if (rc_s != (int)frames) {
      fprintf(stderr,
              "short write, write %d frames\n", rc_s);
    }
    return true;
}
bool SoundProcessor::SetUpPlayer(){
/* Open PCM device for playback. */
  rc_s = snd_pcm_open(&handle_s, "default",
                    SND_PCM_STREAM_PLAYBACK, 0);
  if (rc_s < 0) {
    fprintf(stderr,
            "unable to open pcm device: %s\n",
            snd_strerror(rc_s));
    exit(1);
  }

  /* Allocate a hardware parameters object. */
  snd_pcm_hw_params_alloca(&params_s);

  /* Fill it in with default values. */
  snd_pcm_hw_params_any(handle_s, params_s);

  /* Set the desired hardware parameters. */

  /* Interleaved mode */
  snd_pcm_hw_params_set_access(handle_s, params_s,
                      SND_PCM_ACCESS_RW_INTERLEAVED);

  /* Signed 16-bit little-endian format */
  snd_pcm_hw_params_set_format(handle_s, params_s,
                              SND_PCM_FORMAT_U8);

  /* Two channels (stereo) */
  snd_pcm_hw_params_set_channels(handle_s, params_s, 1);

  /* 44100 bits/second sampling rate (CD quality) */
  val_s = 8000;
  snd_pcm_hw_params_set_rate_near(handle_s, params_s,
                                  &val_s, &dir_s);

  
  //frames = 32;
  snd_pcm_hw_params_set_period_size_near(handle_s,
                              params_s, &frames, &dir_s);

  /* Write the parameters to the driver */
  rc_s = snd_pcm_hw_params(handle_s, params_s);
  if (rc_s < 0) {
    fprintf(stderr,
            "unable to set hw parameters: %s\n",
            snd_strerror(rc_s));
    exit(1);
  }

  /* Use a buffer large enough to hold one period */
  snd_pcm_hw_params_get_period_size(params_s, &frames,
                                    &dir_s);
  size = frames ; /* 2 bytes/sample, 2 channels */
  buffer_s = (char *) malloc(size);

  /* We want to loop for 5 seconds */
  snd_pcm_hw_params_get_period_time(params_s,
                                    &val_s, &dir_s);
  snd_pcm_sframes_t availp;
  snd_pcm_sframes_t delayp;
  snd_pcm_avail_delay(handle_s,&availp,&delayp);
  printf("%l %l\n",availp,delayp);
  return true;
}
bool SoundProcessor::Record(){
  SetUpRecorder();
  snd_pcm_sframes_t fwdframes=snd_pcm_forwardable(handle_m);
  printf("forwarding %d\n",fwdframes);
  snd_pcm_forward(handle_m,fwdframes);
  while (true) {
    rc_m = snd_pcm_readi(handle_m, buffer_m, frames);
    if (rc_m == -EPIPE) {
      /* EPIPE means overrun */
      fprintf(stderr, "overrun occurred\n");
      snd_pcm_prepare(handle_m);
    } else if (rc_m < 0) {
      fprintf(stderr,
              "error from read: %s\n",
              snd_strerror(rc_m));
    } else if (rc_m != (int)frames) {
      fprintf(stderr, "short read, read %d frames\n", rc_m);
    }
    rc_m = write(1, buffer_m, size);
    if (rc_m != size)
      fprintf(stderr,
              "short write: wrote %d bytes\n", rc_m);
  }

  snd_pcm_drain(handle_m);
  snd_pcm_close(handle_m);
  free(buffer_m);
  return true;
}
bool SoundProcessor::RecordAndSend(int sfd,struct sockaddr_in fraddress){
  SetUpRecorder();//this needs to be here otherwise delay of sound will happen
  usleep(300000);
  while (true) {
    framecnt++;
    rc_m = snd_pcm_readi(handle_m, buffer_m, frames);
    if (rc_m == -EPIPE) {
      /* EPIPE means overrun */
      fprintf(stderr, "overrun occurred\n");
      snd_pcm_prepare(handle_m);
    } else if (rc_m < 0) {
      fprintf(stderr,
              "error from read: %s\n",
              snd_strerror(rc_m));
    } else if (rc_m != (int)frames) {
      fprintf(stderr, "short read, read %d frames\n", rc_m);
    }
    // printf("sending packet %d\n",framecnt);
    stringstream response; 
    response << "voice:"<<framecnt<<colon<<buffer_m;
    sendto(sfd,(void*)response.str().c_str(),response.str().length()+1,0,(SA *) &fraddress,sizeof(fraddress));
  }

  snd_pcm_drain(handle_m);
  snd_pcm_close(handle_m);
  free(buffer_m);
  return true;
}
bool SoundProcessor::SetUpRecorder(){
  rc_m = snd_pcm_open(&handle_m, "default",
                    SND_PCM_STREAM_CAPTURE, 0);
  if (rc_m < 0) {
    fprintf(stderr,
            "unable to open pcm device: %s\n",
            snd_strerror(rc_m));
    exit(1);
  }

  /* Allocate a hardware parameters object. */
  snd_pcm_hw_params_alloca(&params_m);

  /* Fill it in with default values. */
  snd_pcm_hw_params_any(handle_m, params_m);

  /* Set the desired hardware parameters. */

  /* Interleaved mode */
  snd_pcm_hw_params_set_access(handle_m, params_m,
                      SND_PCM_ACCESS_RW_INTERLEAVED);

  /* Signed 16-bit little-endian format */
  snd_pcm_hw_params_set_format(handle_m, params_m,
                              SND_PCM_FORMAT_U8);

  /* Two channels (stereo) */
  snd_pcm_hw_params_set_channels(handle_m, params_m, 1);

  /* 44100 bits/second sampling rate (CD quality) */
  val_m = 8000;
  snd_pcm_hw_params_set_rate_near(handle_m, params_m,
                                  &val_m, &dir_m);

  /* Set period size to 32 frames. */
  //frames = 32;
  snd_pcm_hw_params_set_period_size_near(handle_m,
                              params_m, &frames, &dir_m);

  /* Write the parameters to the driver */
  rc_m = snd_pcm_hw_params(handle_m, params_m);
  if (rc_m < 0) {
    fprintf(stderr,
            "unable to set hw parameters: %s\n",
            snd_strerror(rc_m));
    exit(1);
  }

  /* Use a buffer large enough to hold one period */
  snd_pcm_hw_params_get_period_size(params_m,
                                      &frames, &dir_m);
  size = frames; /* 2 bytes/sample, 2 channels */
  buffer_m = (char *) malloc(size);
  /* We want to loop for 5 seconds */
  snd_pcm_hw_params_get_period_time(params_m,
                                         &val_m, &dir_m);
  return true;
}
