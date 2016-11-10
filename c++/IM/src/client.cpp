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
    default:  abort();//fatal("Unexpected case in switch()");
    }
  }
  if (address == NULL)   
    err_quit("-a was not specified with the address");
  int			sockfd,udpfd;
  struct sockaddr_in	servaddr;
  socklen_t		svlen;
  ClientProcessor clProcessor;
  int on=1,nready,n;
  char				buf[MAXLINE];
  //create tcp socket
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
  
  // Create udp socket
  udpfd = Socket(AF_INET, SOCK_DGRAM, 0);
  bzero(&clProcessor.udpselfaddr, sizeof(clProcessor.udpselfaddr));
  clProcessor.udpselfaddr.sin_family = AF_INET;
  clProcessor.udpselfaddr.sin_port = htons(0);
  clProcessor.InterfaceAddress();
  Inet_pton(AF_INET,clProcessor.interfaceAddress.c_str(), &clProcessor.udpselfaddr.sin_addr);
  Bind(udpfd, (SA *) &clProcessor.udpselfaddr, sizeof(clProcessor.udpselfaddr));
  clProcessor.PortFromSocketFd(udpfd,true);
  bzero(&clProcessor.udpservaddr, sizeof(clProcessor.udpservaddr));
  clProcessor.udpservaddr.sin_family = AF_INET;
  clProcessor.udpservaddr.sin_port = htons(SERV_PORT);
  Inet_pton(AF_INET, address, &clProcessor.udpservaddr.sin_addr);
  svlen = sizeof(clProcessor.udpservaddr);
	
  clProcessor.client[0].fd = fileno(stdin);
  clProcessor.client[0].events = POLLIN;
  clProcessor.clLogin[0]=true;
  clProcessor.clUID[0]="stdInput";
  //tcp
  clProcessor.client[1].fd = sockfd;
  clProcessor.client[1].events = POLLIN;
  clProcessor.clLogin[1]=true;
  clProcessor.clUID[1]="server_tcp";
  //udp
  clProcessor.client[2].fd = udpfd;
  clProcessor.client[2].events = POLLIN;
  clProcessor.clLogin[2]=true;
  clProcessor.PortFromSocketFd(udpfd,true);
  clProcessor.clUID[2]="server_udp";
  printf("next command:");
  fflush(NULL);
 	for (int i = 3; i < myOPEN_MAX; i++){
 		clProcessor.client[i].fd = -1;		/* -1 indicates available entry */
		clProcessor.clLogin[i]=false;
		clProcessor.clUID[i]="";
	}
		clProcessor.maxi = 2;					/* max index into client[] array */
 		for ( ; ; ) {
		  nready = Poll(clProcessor.client, clProcessor.maxi+1, INFTIM);
		  for (int i = 0; i <= clProcessor.maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = clProcessor.client[i].fd) < 0)
		      continue;
		    if (clProcessor.client[i].revents & (POLLIN | POLLERR)) {
		       if(i==2){// incoming udp data
			 clProcessor.ProcessUdp();
		       }
		       else if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
			  printf("clProcessor.client[%d] aborted connection\n", i);
			  Close(sockfd);
			  clProcessor.client[i].fd = -1;
			  clProcessor.clLogin[i]=false;
			  clProcessor.clUID[i]="";
			} else
			  err_sys("read error");
		      } else if (n == 0) {
			/*4connection closed by client */
			printf("clProcessor.client[%d] closed connection\n", i);
			Close(sockfd);
			clProcessor.client[i].fd = -1;
			clProcessor.clLogin[i]=false;
			clProcessor.clUID[i]="";
		      }
		      else if (i == 0) { //stdin
			if(clProcessor.chat){
			  Sendto(clProcessor.client[2].fd,(void*)buf,n,0,(SA *) &clProcessor.fraddress,sizeof(clProcessor.fraddress));
			   printf("chat text:");
			   fflush(NULL);
			}
			else{
			  string tmp=buf;
			  string nextCommand=tmp.substr(0,n-1);
			  if(clProcessor.registeredLogedin)
			    clProcessor.RequestToServer(nextCommand);
			  else
			    clProcessor.Register(nextCommand);
			}
		      }
		      else if (i == 1) { //server
			if(!clProcessor.selfaddress)
			  clProcessor.PortFromSocketFd(sockfd,false);
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
