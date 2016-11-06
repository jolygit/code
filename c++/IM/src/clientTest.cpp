// #include <poll.h>
// #include <stdio.h>
// #include <errno.h>
// #include <string.h>
// #include <unistd.h>

// int main()
// {
//   int ret_poll; ssize_t ret_read;
//   struct pollfd input[1] = {{fd: 0, events: POLLIN}};
//   char buff[100];
//   while(1) {
//     ret_poll = poll(input, 1, -1);
//     printf("ret_poll:\t%d\nerrno:\t%d\nstrerror:\t%s\n",
//         ret_poll, errno, strerror(errno));
//     ret_read = read(0, buff, 99);
//     printf("ret_read:\t%zd\nerrno:\t%d\nstrerror:\t%s\nbuff:\t%s\n",
//         ret_read, errno, strerror(errno), buff);
//   }
// }
#include <string.h>
 #include <errno.h>
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
  // int ret_poll; ssize_t ret_read;
  // struct pollfd input[1] = {{fd: 0, events: POLLIN}};
  // char buff[100];
  // while(1) {
  //   ret_poll = poll(input, 1, -1);
  //   printf("ret_poll:\t%d\nerrno:\t%d\nstrerror:\t%s\n",
  // 	   ret_poll, errno, strerror(errno));
  //   ret_read = read(0, buff, 99);
  //   printf("ret_read:\t%zd\nerrno:\t%d\nstrerror:\t%s\nbuff:\t%s\n",
  // 	   ret_read, errno, strerror(errno), buff);
  // }
   int			sockfd;
   int const  myOPEN_MAX=1,MAXLINE=4096;
   struct pollfd	  client[myOPEN_MAX];
   int maxi,nready,n;
   char buf[4096];
   client[0].fd = 0;//fileno(stdin);
   client[0].events = POLLIN;//POLLRDNORM;
   for (int i = 1; i < myOPEN_MAX; i++){
     client[i].fd = -1;		/* -1 indicates available entry */
   }
   maxi = 1;					/* max index into client[] array */
   printf("first line\n");
   for ( ; ; ) {
     nready = poll(client, maxi, -1);
     // printf("enter something\n");
     for (int i = 0; i <= maxi; i++) {	/* check all clients for data */
       if ( (sockfd = client[i].fd) < 0)
   	continue;
       if (client[i].revents & (POLLIN | POLLERR)) {
   	if ( (n = read(sockfd, buf, MAXLINE)) > 0) {
   	  if (i == 0) { 
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
