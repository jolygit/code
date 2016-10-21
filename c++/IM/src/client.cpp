#include	"unp.h"
#include <stdio.h>
#include <boost/algorithm/string.hpp>
#include <ctype.h>
#include "tlpi_hdr.h"
#define printable(ch) (isprint((unsigned char) ch) ? ch : '#')

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
  struct linger		ling;
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
	//str_cli(stdin, sockfd);		/* do it all */

	ling.l_onoff = 1; 
	ling.l_linger = 0; 
	Setsockopt(sockfd, SOL_SOCKET, SO_LINGER, &ling, sizeof(ling));

	exit(0);
} 
