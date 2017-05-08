/* include fig01 */
#include        "serverProcessor.h"
int
main(int argc, char **argv)
{
  int					i, maxi, listenfd, connfd, sockfd,udpfd;
	int					nready;
	ssize_t				n;
	char				buf[MAXLINE];
	socklen_t			clilen;
	int myOPEN_MAX = sysconf (_SC_OPEN_MAX);
	printf("%d\n",myOPEN_MAX);
	struct pollfd		client[myOPEN_MAX];
	struct sockaddr_in	cliaddr, servaddr;
       	ServerProcessor proc;
	// Create tcp socket
	listenfd = Socket(AF_INET, SOCK_STREAM, 0);
	int on=1;
	if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
	  err_sys("setsockopt of SO_REUSEADDR error");
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family      = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port        = htons(SERV_PORT);
	Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));
	// Create udp socket
	udpfd = Socket(AF_INET, SOCK_DGRAM, 0);
	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family      = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port        = htons(SERV_PORT);
	Bind(udpfd, (SA *) &servaddr, sizeof(servaddr));
	
 	Listen(listenfd, LISTENQ);
 	client[0].fd = listenfd;
 	client[0].events = POLLIN;
	proc.clLogin[0]=false;
	proc.clUID[0]="";
	client[1].fd = udpfd;// all different clients will be sending udp to this socket
 	client[1].events = POLLIN;
	proc.clLogin[1]=false;
	proc.clUID[1]="";
 	for (i = 2; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
		proc.clLogin[i]=false;
		proc.clUID[i]="";
	}
		maxi = 1;					/* max index into client[] array */
 
		for ( ; ; ) {
		  nready = Poll(client, maxi+1, INFTIM);
		  if (client[0].revents & POLLIN) {	/* new client connection */
		    clilen = sizeof(cliaddr);
		    connfd = Accept(listenfd, (SA *) &cliaddr, &clilen);
		    //#ifdef	NOTDEF
		    printf("new client: %s\n", Sock_ntop((SA *) &cliaddr, clilen));
		    //#endif

		    for (i = 2; i < myOPEN_MAX; i++)
		      if (client[i].fd < 0) {
			client[i].fd = connfd;	/* save descriptor */
			break;
		      }
		    if (i == myOPEN_MAX)
		      err_quit("too many clients");

		    client[i].events = POLLIN;
		    if (i > maxi)
		      maxi = i;				/* max index in client[] array */

		    if (--nready <= 0)
		      continue;				/* no more readable descriptors */
		  }
		  string  invitefriend="invitefriend",friendaddress="friendaddress",
  allfriends="allfriends",onlinefriends="onlinefriends";
		  for (i = 1; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLIN | POLLERR)) {
		      if(i==1){// udp connection to server
			n=Recvfrom(sockfd,buf, MAXLINE,0,(SA *) &cliaddr, &clilen);
			char* clAddress=Sock_ntop((SA *) &cliaddr, clilen);
			printf("udp connection from client %s:%s\n",clAddress,buf);
			if(proc.AddUdpToDatabase(clAddress,buf)==0){
			  string resp="received your udp request successfuly.\n";
			  Sendto(sockfd,(void*)resp.c_str(),resp.length()+1,0,(SA *) &cliaddr, clilen);
			}
		      }
		      else if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
			  printf("client[%d] aborted connection\n", i);
			  Close(sockfd);
			  client[i].fd = -1;
			  proc.clLogin[i]=false;
			  proc.clUID[i]="";
			} else
			  err_sys("read error");
		      }
		      else if (n == 0) {
			//when someone closes connection we notify its online friends aobut it
			//before the connection is closed we want to retreive the connections online friends 
			string frlist;
			proc.GetFriends(onlinefriends,i,frlist);
			vector<string> frs;
			boost::split(frs,frlist,boost::is_any_of(","));
			/*4connection closed by client */
			printf("client[%d] closed connection\n", i);
			Close(sockfd);
			client[i].fd = -1;
			proc.clLogin[i]=false;
			string userofline=proc.clUID[i];
			proc.clUID[i]="";
			//now that connection i is closed get online frinds below will not return it as part of the list
			for(int f=0;f<frs.size();f++){
			  string toUsername=frs[f];
			  for(int u=0;u<=maxi;u++){
			    if(proc.clUID[u]==toUsername && proc.clLogin[u]){//notify toUsername about proc.clUID[i] getting online 
			      string frlist;
			      proc.GetFriends(onlinefriends,u,frlist);// retreive updated online friend list
			      proc.SendResponse(client[u].fd,onlinefriends,frlist);
			      printf("updateing  %s about %s going offline\n",toUsername.c_str(),userofline.c_str());
			      break;
			    }
			  }
			}
		      }
		      else if(proc.clLogin[i]==false && i!=1){
			char* clAddress=Sock_ntop((SA *) &cliaddr, clilen);
			if(proc.RegisterOrLogin(sockfd,proc.clUID[i],buf,clAddress)==0)
			  proc.clLogin[i]=true;
			if(proc.clLogin[i]){
			  proc.ProcessFriendRequests(proc.clUID[i],sockfd);
			  //when the user first gets online we want to notify its online friends about it. That is waht is done below
			  string frlist;
			  proc.GetFriends(onlinefriends,i,frlist);
			  vector<string> frs;
			  boost::split(frs,frlist,boost::is_any_of(","));
			  for(int f=0;f<frs.size();f++){
			    string toUsername=frs[f];
			    for(int u=0;u<=maxi;u++){
			      if(proc.clUID[u]==toUsername && proc.clLogin[u]){//notify toUsername about proc.clUID[i] getting online 
				 string frlist;
				 proc.GetFriends(onlinefriends,u,frlist);
				 proc.SendResponse(client[u].fd,onlinefriends,frlist);
				 printf("updateing  %s about %s comming online\n",toUsername.c_str(),proc.clUID[i].c_str());
				 break;
			      }
			    }
			  }
			}
		      }
		    
		      else{
			string str(buf);
			vector<string> req;
			boost::split(req,str,boost::is_any_of(":"));
			if(req[1]==invitefriend && proc.clUID[i]!=""){
			  string toUsername=req[2];
			  string msg;
			  if(proc.FriendRequest(proc.clUID[i],toUsername))
			    msg="request failed because user does not exist";
			  else{
			    msg="request filed sucessfully";
			    for(int u=0;u<=maxi;u++){
			      if(proc.clUID[u]==toUsername && proc.clLogin[u]){//notify toUsername about request since it is online
				proc.ProcessFriendRequests(toUsername,client[u].fd);
				printf("notifying %s about the friendship request from %s\n",toUsername.c_str(),proc.clUID[i].c_str());
				break;
			      }
			    }
			  }
			  proc.SendResponse(sockfd,req[1],msg);
			}
			if((req[1]==allfriends || req[1]==onlinefriends) && proc.clUID[i]!=""){
			  string msg;
			  proc.GetFriends(req[1],i,msg);
			  proc.SendResponse(sockfd,req[1],msg);
			}
			if((req[1]=="accept") && proc.clUID[i]!=""){
			  string user=req[2];
			  proc.CreateFriends(proc.clUID[i],user,true);
			  proc.CreateFriends(user,proc.clUID[i],true);
			  string msg;
			  proc.GetFriends(allfriends,i,msg);
			  proc.SendResponse(sockfd,allfriends,msg);//sending response to the one who accepted
			  for(int u=1;u<=maxi;u++){
			    if(proc.clUID[u]==user && proc.clLogin[u]){
			      string msg;
			      proc.GetFriends(allfriends,u,msg);
			      proc.SendResponse(client[u].fd,allfriends,msg);//send update to the user who requested firendship
			      break;
			    }
			  }
			  printf("%s and %s are now friends\n",proc.clUID[i].c_str(),user.c_str());
			}
			if((req[1]=="reject") && proc.clUID[i]!=""){
			  string user=req[2];
			  proc.CreateFriends(proc.clUID[i],user,false);
			  proc.CreateFriends(user,proc.clUID[i],false);
			  printf("%s and %s are rejected\n",proc.clUID[i].c_str(),user.c_str());
			}
			if(req[1]==friendaddress){
			  string fruid=req[2];
			  for(int ii=0;ii<myOPEN_MAX;ii++){
			    if(proc.clUID[ii]==fruid && client[ii].fd){
			      bool sameNet=proc.OnTheSameNet(proc.clUID[i],fruid);
			      proc.InitiateHolePunch(client[ii].fd,proc.clUID[i],true,sameNet);
			      proc.InitiateHolePunch(sockfd,fruid,true,sameNet);
			      break;
			    }
			  }
			}
		      }
		      if (--nready <= 0)
			break;				/* no more readable descriptors */
		    }
		  }
		}
}



