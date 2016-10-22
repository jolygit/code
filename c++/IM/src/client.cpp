#include	"unp.h"
#include <stdio.h>
#include <stdlib.h>
#include <boost/algorithm/string.hpp>
#include <ctype.h>
//#include "tlpi_hdr.h"
#include        <vector>
#define printable(ch) (isprint((unsigned char) ch) ? ch : '#')
using namespace std;
extern "C"{
  int Socket(int, int, int);
  void Bind(int, sockaddr const*, unsigned int);
  void Listen(int, int);
  int Poll(pollfd*, unsigned long, int);
  int Accept(int, sockaddr*, unsigned int*);
  void err_quit(char const*, ...);
  void Close(int);
  void err_sys(char const*, ...);
  void Writen(int, void*, unsigned long);
  ssize_t Readn(int, void*, size_t);
  ssize_t Read(int, void*, size_t);
  void	 Connect(int, const SA *, socklen_t);
  void	 Inet_pton(int, const char *, void *);
  void	 str_cli(FILE *, int);
  void	 Setsockopt(int, int, int, const void *, socklen_t);
  char	*Fgets(char *, int, FILE *);
  char	*Sock_ntop(const SA *, socklen_t);
  void	 Getsockname(int, SA *, socklen_t *);
}
int Register(int sockfd,bool registering){
  char RBuffer[500];
  char reply[500];
  char username[20];
  char password[20];
  char firstName[40];
  char lastName[40];
  char email[80];
  printf("username: ");
  scanf("%s", username);
  printf("password: ");
  scanf("%s", password);
  //  boost::trim_right(password);
  if(registering){
  printf("firstName: ");
  scanf("%s", firstName);
  printf("lastName: ");
  scanf("%s", lastName);
  printf("email: ");
  scanf("%s", email);
  // boost::trim_right(email);
  snprintf(RBuffer,500,"Register::,%s,%s,%s,%s,%s",username,password,firstName,lastName,email);
  }
   else
     snprintf(RBuffer,500,"Login::,%s,%s",username,password);
  Writen(sockfd, RBuffer, sizeof(RBuffer));
  if (Read(sockfd, reply, sizeof(reply)) >0){
    if(strcmp(reply,"ok")==0){
      if(registering)
	printf("sucessfully registered\n");
      else
	printf("sucessfully loged in\n");
    }
    else{
      printf("%s\n",reply);
       Register(sockfd,registering);
    }
  }
  return 0;
}

static void             /* Print "usage" message and exit */
usageError(char *progName, char *msg, int opt)
{
    if (msg != NULL && opt != 0)
        fprintf(stderr, "%s (-%c)\n", msg, printable(opt));
    fprintf(stderr, "Usage: %s [-p arg] [-x]\n", progName);
    exit(EXIT_FAILURE);
}
int ClientServer(int sockfd);
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
	sockfd = Socket(AF_INET, SOCK_STREAM, 0);
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(SERV_PORT);
	Inet_pton(AF_INET, address, &servaddr.sin_addr);
 	Connect(sockfd, (SA *) &servaddr, sizeof(servaddr));
	if(uregister)
	  Register(sockfd,true);
	else if(login)
	  Register(sockfd,false);
	else{
	  printf("you have to either register or login use -r or -l options respectively\n");
	}
	ClientServer(sockfd);
}
int PortFromSocketFd(int socketFd){
  struct sockaddr ownAddr;
  socklen_t len;
  len=sizeof(ownAddr);
  Getsockname(socketFd,(SA*)&ownAddr,&len);
  char* ownAddress=Sock_ntop((SA *) &ownAddr, len);
  vector<string> ip_port;
  boost::split(ip_port,ownAddress,boost::is_any_of(":"));
  string ip;
  string port;
  ip=ip_port[0];
  port=ip_port[1];
  printf("parsed ip and port of client %s %s\n",ip.c_str(),port.c_str());
  return atoi(port.c_str());
}
int ClientServer(int ownsockfd){
        int				nready,i, maxi, listenfd, connfd, sockfd;
	ssize_t				n;
	char				buf[MAXLINE];
	socklen_t			clilen;
	int myOPEN_MAX = sysconf (_SC_OPEN_MAX);
	int port=PortFromSocketFd(ownsockfd);
	  //	printf("%d\n",myOPEN_MAX);
	struct pollfd		client[myOPEN_MAX];
 	client[0].fd = ownsockfd;
 	client[0].events = POLLRDNORM;

 	for (i = 2; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
	}
		maxi = 0;					/* max index into client[] array */
		for ( ; ; ) {
		  nready = Poll(client, maxi+1, INFTIM);
		  for (i = 0; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLRDNORM | POLLERR)) {
		      if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
			  printf("client[%d] aborted connection\n", i);
			  Close(sockfd);
			  client[i].fd = -1;
			} else
			  err_sys("read error");
		      } else if (n == 0) {
			/*4connection closed by client */
			printf("client[%d] closed connection\n", i);
			Close(sockfd);
			client[i].fd = -1;
		      }
		      else{
			//	Writen(sockfd, buf, n);
		      }
		      if (--nready <= 0)
			break;				/* no more readable descriptors */
		    }
		  }
		}
  return 0;
}
