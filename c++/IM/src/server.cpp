/* include fig01 */
#include        "serverProcessor.h"
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
       	ServerProcessor proc;
	listenfd = Socket(AF_INET, SOCK_STREAM, 0);
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family      = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port        = htons(SERV_PORT);
	Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));
 	Listen(listenfd, LISTENQ);
 	client[0].fd = listenfd;
 	client[0].events = POLLRDNORM;
	proc.clLogin[0]=false;
	proc.clUID[0]="";
 	for (i = 1; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
		proc.clLogin[i]=false;
		proc.clUID[i]="";
	}
		maxi = 0;					/* max index into client[] array */
 
		for ( ; ; ) {
		  nready = Poll(client, maxi+1, INFTIM);
		  if (client[0].revents & POLLRDNORM) {	/* new client connection */
		    clilen = sizeof(cliaddr);
		    connfd = Accept(listenfd, (SA *) &cliaddr, &clilen);
		    //#ifdef	NOTDEF
		    printf("new client: %s\n", Sock_ntop((SA *) &cliaddr, clilen));
		    //#endif

		    for (i = 1; i < myOPEN_MAX; i++)
		      if (client[i].fd < 0) {
			client[i].fd = connfd;	/* save descriptor */
			break;
		      }
		    if (i == myOPEN_MAX)
		      err_quit("too many clients");

		    client[i].events = POLLRDNORM;
		    if (i > maxi)
		      maxi = i;				/* max index in client[] array */

		    if (--nready <= 0)
		      continue;				/* no more readable descriptors */
		  }

		  for (i = 1; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLRDNORM | POLLERR)) {
		      if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
			  printf("client[%d] aborted connection\n", i);
			  Close(sockfd);
			  client[i].fd = -1;
			  proc.clLogin[i]=false;
			  proc.clUID[i]="";
			} else
			  err_sys("read error");
		      } else if (n == 0) {
			/*4connection closed by client */
			printf("client[%d] closed connection\n", i);
			Close(sockfd);
			client[i].fd = -1;
			proc.clLogin[i]=false;
			proc.clUID[i]="";
		      }
		      else if(proc.clLogin[i]==false){
			//	printf("%s",buf);
			char* clAddress=Sock_ntop((SA *) &cliaddr, clilen);
			if(proc.RegisterOrLogin(sockfd,proc.clUID[i],buf,clAddress)==0)
			  proc.clLogin[i]=true;
			if(proc.clLogin[i])
			  proc.ProcessFriendRequests(proc.clUID[i]);
		      }
		      else{
			string str(buf);
			if(str.compare(0,14,"requestFriend:")==0 && proc.clUID[i]!=""){
			  vector<string> req;
			  boost::split(req,str,boost::is_any_of(":"));
			  string toUsername=req[1];
			  string msg;
			  if(proc.FriendRequest(proc.clUID[i],toUsername))
			    msg="request failed because user does not exist";
			  else
			    msg="request filed sucessfully";
			  Writen(sockfd,(void*)msg.c_str(),msg.length()+1);
			}
			if(strcmp(buf,"allfriends")==0 && proc.clUID[i]!=""){
			  int len=0;
			  string friendreq="allfriends";
			  string value;
			  proc.GetFriends(friendreq,i,value);
			  printf("sending this many bytes%d\n",value.length()+1);
			  if(proc.Send_int(value.length()+1,sockfd)==0)
			    Writen(sockfd,(void*)value.c_str(),value.length()+1);
			}
			if(str.compare(0,14,"friendAddress:")==0){
			  vector<string> strs;
			  boost::split(strs,str,boost::is_any_of(":"));
			  string fruid=strs[1];
			  int len=0;
			  string address;
			  proc.GetAddress(fruid,address);
			  printf("sending this many bytes%d\n",address.length()+1);
			  if(proc.Send_int(address.length()+1,sockfd)==0)
			    Writen(sockfd,(void*)address.c_str(),address.length()+1);
			}
			if(strcmp(buf,"onlinefriends")==0 && proc.clUID[i]!=""){
			  int len=0;
			  string friendreq="onlinefriends";
			  string value;
			  proc.GetFriends(friendreq,i,value);
			  printf("sending this many bytes%d\n",value.length()+1);
			  if(proc.Send_int(value.length()+1,sockfd)==0)
			    Writen(sockfd,(void*)value.c_str(),value.length()+1);
			  // sendOnlineFriends();
			}
		      }

		      if (--nready <= 0)
			break;				/* no more readable descriptors */
		    }
		  }
		}
}



