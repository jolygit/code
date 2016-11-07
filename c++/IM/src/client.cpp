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
  struct pollfd	  client[myOPEN_MAX];
  int on=1,maxi,nready,n;
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
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEADDR error");
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEPORT error");
  bzero(&servaddr, sizeof(servaddr));
  servaddr.sin_family = AF_INET;
  servaddr.sin_port = htons(SERV_PORT);
  Inet_pton(AF_INET, address, &servaddr.sin_addr);
  svlen = sizeof(servaddr);
	
  client[0].fd = fileno(stdin);
  client[0].events = POLLIN;
  clProcessor.clLogin[0]=true;
  clProcessor.clUID[0]="stdInput";
  //tcp
  client[1].fd = sockfd;
  client[1].events = POLLIN;
  clProcessor.clLogin[1]=true;
  clProcessor.clUID[1]="server_tcp";
  //udp
  client[2].fd = udpfd;
  client[2].events = POLLIN;
  clProcessor.clLogin[2]=true;
  clProcessor.clUID[2]="server_udp";
  printf("next command:");
  fflush(NULL);
 	for (int i = 3; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
		clProcessor.clLogin[i]=false;
		clProcessor.clUID[i]="";
	}
		maxi = 3;					/* max index into client[] array */
 		for ( ; ; ) {
		  nready = Poll(client, maxi, INFTIM);
		  for (int i = 0; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLIN | POLLERR)) {
		       if(i==2){// udp response from server
			 n=Recvfrom(sockfd,buf,MAXLINE, 0,(SA *) &servaddr, &svlen);
			char* svAddress=Sock_ntop((SA *) &servaddr, svlen);
			printf("udp response from server %s: %s\n",svAddress,buf);
			printf("next command:");
			fflush(NULL);
		      }
		       else if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
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
			string startudp="startudp";
			if(nextCommand==startudp){
			  Sendto(udpfd,(void*)startudp.c_str(),startudp.length()+1,0,(SA *) &servaddr, svlen);
			}
			else if(clProcessor.registeredLogedin)
			  clProcessor.RequestToServer(client[1].fd,nextCommand);
			else
			  clProcessor.Register(client[1].fd,nextCommand);
		      }
		      else if (i == 1) { //server
			if(!clProcessor.selfaddress)
			  clProcessor.PortFromSocketFd(sockfd);
			string fruid;
			int connfd=clProcessor.ResponseFromServer(buf,fruid);
			if(connfd){
			  int ii;
			  for (ii = 1; ii < myOPEN_MAX; ii++)
			    if (client[ii].fd < 0) {
			      client[ii].fd = connfd;	/* save descriptor */
			      clProcessor.clLogin[1]=true;
			      clProcessor.clUID[1]=fruid;
			      break;
			    }
			  if (ii == myOPEN_MAX)
			    err_quit("too many clients");
			  client[ii].events = POLLIN;
			  if (ii > maxi)
			    maxi = ii;
			  printf("sucessfully connected to: %s\n",fruid.c_str());
			}
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
