#include	"unp.h"
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
  void	 Connect(int, const SA *, socklen_t);
  void	 Inet_pton(int, const char *, void *);
  void	 str_cli(FILE *, int);
  void	 Setsockopt(int, int, int, const void *, socklen_t);
}
int
main(int argc, char **argv)
{
	int					sockfd;
	struct linger		ling;
	struct sockaddr_in	servaddr;

	if (argc != 2)   
		err_quit("usage: tcpcli <IPaddress>");

	sockfd = Socket(AF_INET, SOCK_STREAM, 0);

	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_port = htons(SERV_PORT);
	Inet_pton(AF_INET, argv[1], &servaddr.sin_addr);
 
	Connect(sockfd, (SA *) &servaddr, sizeof(servaddr));

	str_cli(stdin, sockfd);		/* do it all */

	ling.l_onoff = 1; 
	ling.l_linger = 0; 
	Setsockopt(sockfd, SOL_SOCKET, SO_LINGER, &ling, sizeof(ling));

	exit(0);
} 
