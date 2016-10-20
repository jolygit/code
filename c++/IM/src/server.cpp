/* include fig01 */
#include	"unp.h"
#include	<limits.h>		/* for OPEN_MAX */
#include        "database.h"
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
}
int
main(int argc, char **argv)
{
	int					i, maxi, listenfd, connfd, sockfd;
	int					nready;
	ssize_t				n;
	char				buf[MAXLINE];
	socklen_t			clilen;
	int myOPEN_MAX = sysconf (_SC_OPEN_MAX);
	printf("%d\n",myOPEN_MAX);
	struct pollfd		client[myOPEN_MAX];
	struct sockaddr_in	cliaddr, servaddr;

	listenfd = Socket(AF_INET, SOCK_STREAM, 0);

	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family      = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port        = htons(SERV_PORT);
        DataBase db;   
	db.ConnectToCollection("users","users");
	db.AddArrayKeyValueToDocument("username","givi","friends","username","aj476");
	db.RemoveArrayKeyValueFromDocument("username","givi","friends","username","aj476");
	if(db.FindKeyValueInArrayOfDocument("username","givi","friends","username","aj476"))
	  printf("found\n");
	if(!db.FindKeyValueInArrayOfDocument("username","givi","friends","username1","aj476"))
	  printf("not found\n");
	if(!db.FindKeyValueInArrayOfDocument("username1","givi","friends","username","aj476"))
	  printf("not found\n");
	if(!db.FindKeyValueInArrayOfDocument("username","givi1","friends","username","aj476"))
	  printf("not found\n");
	if(!db.FindKeyValueInArrayOfDocument("username","givi","friends","username","aj4761"))
	  printf("not found\n");

	// Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));

// 	Listen(listenfd, LISTENQ);

// 	client[0].fd = listenfd;
// 	client[0].events = POLLRDNORM;
// 	for (i = 1; i < myOPEN_MAX; i++)
// 		client[i].fd = -1;		/* -1 indicates available entry */
// 	maxi = 0;					/* max index into client[] array */
// /* end fig01 */

// /* include fig02 */
// 	for ( ; ; ) {
// 		nready = Poll(client, maxi+1, INFTIM);

// 		if (client[0].revents & POLLRDNORM) {	/* new client connection */
// 			clilen = sizeof(cliaddr);
// 			connfd = Accept(listenfd, (SA *) &cliaddr, &clilen);
// #ifdef	NOTDEF
// 			printf("new client: %s\n", Sock_ntop((SA *) &cliaddr, clilen));
// #endif

// 			for (i = 1; i < myOPEN_MAX; i++)
// 				if (client[i].fd < 0) {
// 					client[i].fd = connfd;	/* save descriptor */
// 					break;
// 				}
// 			if (i == myOPEN_MAX)
// 				err_quit("too many clients");

// 			client[i].events = POLLRDNORM;
// 			if (i > maxi)
// 				maxi = i;				/* max index in client[] array */

// 			if (--nready <= 0)
// 				continue;				/* no more readable descriptors */
// 		}

// 		for (i = 1; i <= maxi; i++) {	/* check all clients for data */
// 			if ( (sockfd = client[i].fd) < 0)
// 				continue;
// 			if (client[i].revents & (POLLRDNORM | POLLERR)) {
// 				if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
// 					if (errno == ECONNRESET) {
// 							/*4connection reset by client */
// #ifdef	NOTDEF
// 						printf("client[%d] aborted connection\n", i);
// #endif
// 						Close(sockfd);
// 						client[i].fd = -1;
// 					} else
// 						err_sys("read error");
// 				} else if (n == 0) {
// 						/*4connection closed by client */
// #ifdef	NOTDEF
// 					printf("client[%d] closed connection\n", i);
// #endif
// 					Close(sockfd);
// 					client[i].fd = -1;
// 				} else
// 					Writen(sockfd, buf, n);

// 				if (--nready <= 0)
// 					break;				/* no more readable descriptors */
// 			}
// 		}
// 	}
}
/* end fig02 */
