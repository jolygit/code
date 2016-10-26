#include "clientProcessor.h"

int ClientProcessor::Commands(int sockfd){
  string nextCommand;
  printf("next command: ");
  cin >> nextCommand;
  // scanf("%s", nextCommand);
  if(nextCommand==string("allfriends")){
    ListFriends("allfriends",sockfd);
  }
  else if(nextCommand==string("onlinefriends")){
    ListFriends("onlinefriends",sockfd);
  }
  else if(nextCommand==string("requestfriend")){
    printf("friend username: ");
    cin >> nextCommand;
    RequestFriend(nextCommand,sockfd);
  }
  else if(nextCommand==string("connectto")){
    //ConnectToFriend();
    
  }
  else{
    printf("supported commands are:allfriends;requestfriend;onlinefriends;connectto <friendname>. Try again\n");
  }
  Commands(sockfd);
  return 0;
}
int ClientProcessor::receive_int(int *num, int fd)
{
    int32_t ret;
    char *data = (char*)&ret;
    int left = sizeof(ret);
    int rc;
    do {
        rc = read(fd, data, left);
        if (ret <= 0) {
            if ((errno == EAGAIN) || (errno == EWOULDBLOCK)) {
                // use select() or epoll() to wait for the socket to be readable again
            }
            else if (errno != EINTR) {
                return -1;
            }
        }
        else {
            data += rc;
            left -= rc;
        }
    }
    while (left > 0);
    *num = ntohl(ret);
    return 0;
}
int ClientProcessor::ListFriends(char* req,int sockfd){
  string request(req);
  Writen(sockfd, (void*)request.c_str(),request.length()+1);
  int repLenght;
  if(receive_int(&repLenght,sockfd)!=0)
    printf("could not read lenght\n");
  char reply[repLenght];
  int len=0;
  if ((len=Readn(sockfd, reply,repLenght)) >0){
    string  str(reply);
    vector<string> strs;
    boost::split(strs,str,boost::is_any_of(","));
    if(repLenght==len){
     for (size_t i = 0; i < strs.size(); i++)
        cout << strs[i] << endl;
    }
    else{
      printf("something is wrong\n");
      return 1;
    }
  }
  return 0;
}
int ClientProcessor::RequestFriend(string& req,int sockfd){
  string request("requestFriend:");
  char* response[60];
  request+=req;
  Writen(sockfd, (void*)request.c_str(),request.length()+1);
  if(Read(sockfd,response,sizeof(response))>0)
     printf("%s\n",response);
  return 0;
}
int ClientProcessor::Register(int sockfd,bool registering){
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

// static void             /* Print "usage" message and exit */
// usageError(char *progName, char *msg, int opt)
// {
//     if (msg != NULL && opt != 0)
//         fprintf(stderr, "%s (-%c)\n", msg, printable(opt));
//     fprintf(stderr, "Usage: %s [-p arg] [-x]\n", progName);
//     exit(EXIT_FAILURE);
// }
int ClientProcessor::PortFromSocketFd(int socketFd){
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
int ClientProcessor::ClientServer(int ownsockfd){
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
