#include "clientProcessor.h"

int
main(int argc, char **argv)
{
  int opt,r,l;
  bool uregister=false,login=false;
  char *address=NULL;
  while ((opt = getopt(argc, argv, ":a:rlx")) != -1) {
    switch (opt) {
    case 'a': address = optarg;     break;
    case 'r': uregister=true;       break;
    case 'l': login=true;           break;
      // case ':': usageError(argv[0], "Missing argument", optopt);
      //  case '?': usageError(argv[0], "Unrecognized option", optopt);
    default:  abort();//fatal("Unexpected case in switch()");
    }
  }
  if (address == NULL)   
    err_quit("-a was not specified with the address");
  int			sockfd;
  struct sockaddr_in	servaddr;
  ClientProcessor clProcessor;
  struct pollfd	  client[myOPEN_MAX];
  int on=1,maxi,nready,n;
  char				buf[MAXLINE];
  sockfd = Socket(AF_INET, SOCK_STREAM, 0);
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEADDR error");
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEPORT error");
  bzero(&servaddr, sizeof(servaddr));
  servaddr.sin_family = AF_INET;
  servaddr.sin_port = htons(SERV_PORT);
  Inet_pton(AF_INET, address, &servaddr.sin_addr);
  Connect(sockfd, (SA *) &servaddr, sizeof(servaddr));
 
  client[0].fd = fileno(stdin);
  client[0].events = POLLRDNORM;
  clProcessor.clLogin[0]=true;
  clProcessor.clUID[0]="stdInput";
  client[1].fd = sockfd;
  client[1].events = POLLRDNORM;
  clProcessor.clLogin[1]=true;
  clProcessor.clUID[1]="server";
  printf("next command:");
  fflush(NULL);
 	for (int i = 2; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
		clProcessor.clLogin[i]=false;
		clProcessor.clUID[i]="";
	}
		maxi = 1;					/* max index into client[] array */
 		for ( ; ; ) {
		  nready = Poll(client, maxi+1, INFTIM);
		  for (int i = 0; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLRDNORM | POLLERR)) {
		      if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
			  printf("client[%d] aborted connection\n", i);
			  Close(sockfd);
			  client[i].fd = -1;
			  clProcessor.clLogin[i]=false;
			  clProcessor.clUID[i]="";
			} else
			  err_sys("read error");
		      } else if (n == 0) {
			/*4connection closed by client */
			printf("client[%d] closed connection\n", i);
			Close(sockfd);
			client[i].fd = -1;
			clProcessor.clLogin[i]=false;
			clProcessor.clUID[i]="";
		      }
		      else if (i == 0) { //stdin
			string tmp=buf;
			string nextCommand=tmp.substr(0,n-1);
			//	boost::trim_right(nextCommand);
			if(clProcessor.registeredLogedin)
			  clProcessor.RequestToServer(client[1].fd,nextCommand);
			else
			  clProcessor.Register(client[1].fd,nextCommand);
		      }
		      else if (i == 1) { //server
			if(!clProcessor.selfaddress)
			  clProcessor.PortFromSocketFd(sockfd);
			clProcessor.ResponseFromServer(buf);
		      }
		      else{
		
		      }
		      if (--nready <= 0)
			break;				/* no more readable descriptors */
		    }
		  }
		}
  return 0;
}
