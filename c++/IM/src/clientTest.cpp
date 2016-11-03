#include <sys/ioctl.h>
#include <sys/poll.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
int
main(int argc, char **argv)
{
  int			sockfd;
  int const  myOPEN_MAX=100,MAXLINE=4096;
  struct pollfd	  client[myOPEN_MAX];
  int maxi,nready,n;
  char buf[4096];
  client[0].fd = fileno(stdin);
  client[0].events = POLLRDNORM;
  for (int i = 1; i < myOPEN_MAX; i++){
    client[i].fd = -1;		/* -1 indicates available entry */
  }
  maxi = 1;					/* max index into client[] array */
  printf("enter something\n");
  for ( ; ; ) {
    nready = poll(client, maxi+1, -1);
    for (int i = 0; i <= maxi; i++) {	/* check all clients for data */
      if ( (sockfd = client[i].fd) < 0)
	continue;
      if (client[i].revents & (POLLRDNORM | POLLERR)) {
	if ( (n = read(sockfd, buf, MAXLINE)) > 0) {
	  if (i == 0) { //stdin
	    printf("works\n");
	    return 0;
	  }
	  if (--nready <= 0)
	    break;				/* no more readable descriptors */
	}
      }
    }
  }
  return 0;
}
