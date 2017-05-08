#include "clientProcessor.h"

int ClientProcessor::RequestToServer(string& nextCommand){ //sockfd is server
  string request("request:");
  int sockfd=client[1].fd;
  if(nextCommand==allfriends || nextCommand==onlinefriends || nextCommand.compare(0,14,"friendaddress:")==0 || nextCommand.compare(0,13,"invitefriend:")==0){
    request+=nextCommand;
    Writen(sockfd,(void*)request.c_str(),request.length()+1);
    if(nextCommand.compare(0,14,"friendaddress:")==0){
      chat=true;
    }
  }
  else{
    printf("supported commands are:: allfriends;onlinefriends;invitefriend:<frienduid>;friendaddress:<frienduid>. Try again\n");
    printf("next command:"); 
    fflush(stdout);
  }
  return 0;
}
int ClientProcessor::ResponseFromServer(char* buf){
  string response=buf;
  string command;
  vector<string> strs;
  boost::split(strs,response,boost::is_any_of(":"));
  if(strs[2]==allfriends || strs[2]==onlinefriends){ // version:response size:responseCommand:friend1,friend2,...
    vector<string> strs1;
    boost::split(strs1,strs[3],boost::is_any_of(","));
    for (size_t i = 0; i < strs1.size(); i++)
      cout << strs1[i] << endl;
  }
  else if(strs[2]==invitefriend){
    printf("%s\n",strs[3].c_str()); // this should say weather request was ok or not 
  }
  else if(strs[2]==registration || strs[2]==login || strs[2]==registrationlogin){
    printf("\n%s %s\n",strs[2].c_str(),strs[3].c_str()); // this should say weather request was ok or not 
  }
  else if(strs[2]==friendaddress){// version:response size:responseCommand:friendIp:friendPort:frUid
    // printf("\n connecting to %s...\n",strs[5].c_str());
    // fflush(NULL);
    UDPHolePunch(strs[4],strs[3]);
    // if(record)
      // pthread_create(&tid, NULL, Sound_wrapper, this);//separate thread reads sound from mic and sends to udp socket
      //int sockfd=TcpSimultaneousOpen(strs[4],strs[3],strs[5]);
    return 0;
  }
  else{
    printf("supported commands are:allfriends;requestfriend;onlinefriends;friendaddress:<frienduid>. Try again\n");
  }
  printf("next command:");
  fflush(NULL);
  return 0;
}
int ClientProcessor::StartSendingSound(){
   sp.RecordAndSend(client[2].fd,fraddress);
  return 0;
}
int ClientProcessor::UDPHolePunch(string& friendPort,string& friendIp){
  
  string msg="udp hole punch\n";
  bzero(&fraddress, sizeof(fraddress));
  fraddress.sin_family = AF_INET;
  fraddress.sin_port = htons(atoi(friendPort.c_str()));
  Inet_pton(AF_INET, friendIp.c_str(), &fraddress.sin_addr);
  Sendto(client[2].fd,(void*)msg.c_str(),msg.length(),0,(SA *) &fraddress,sizeof(fraddress));
  chat=true;
  return 0;
}
int ClientProcessor::TcpSimultaneousOpen(string& friendPort,string& friendIp,string& fruid){
  // struct sockaddr_in	fraddress, selfAddress;
  // int			sockfd;
  // sockfd = Socket(AF_INET, SOCK_STREAM, 0);
  // int on=1;
  // if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
  //   err_sys("setsockopt of SO_REUSEADDR error");
  // if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
  //   err_sys("setsockopt of SO_REUSEPORT error");
  // bzero(&selfAddress, sizeof(selfAddress));
  // selfAddress.sin_family      = AF_INET;
  // Inet_pton(AF_INET, selfTcpIp.c_str(), &selfAddress.sin_addr);
  // selfAddress.sin_port        = htons(atoi(selfTcpPort.c_str()));
  // Bind(sockfd, (SA *) &selfAddress, sizeof(selfAddress));
  // bzero(&fraddress, sizeof(fraddress));
  // fraddress.sin_family      = AF_INET;
  // Inet_pton(AF_INET, friendIp.c_str(), &fraddress.sin_addr);
  // fraddress.sin_port        = htons(atoi(friendPort.c_str()));
  // Connect(sockfd, (SA *) &fraddress, sizeof(fraddress));
  // int ii;
  // for (ii = 1; ii < myOPEN_MAX; ii++)
  //   if (client[ii].fd < 0) {
  //     client[ii].fd = sockfd;	/* save descriptor */
  //     clLogin[ii]=true;
  //     clUID[ii]=fruid;
  //     break;
  //   }
  // if (ii == myOPEN_MAX)
  //   err_quit("too many clients");
  // client[ii].events = POLLIN;
  // if (ii > maxi)
  //   maxi = ii;
  printf("sucessfully connected to: %s\n",fruid.c_str());
  return 0;
}

int ClientProcessor::Receive_int(int *num, int fd)
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
int ClientProcessor::Register(string& nextCommand){
  string request;
  int sockfd=client[1].fd;
  if(registrationFieldCount==0){
    if(nextCommand=="register")
      uregister=true;
    else if(nextCommand=="login")
      uregister=false;
    else{
      printf("you have to either register or login. Type register or login respectively\n");
      printf("next command:");
      fflush(stdout);
      return 1;
    }
    printf("username:");
    fflush(stdout);
    registrationFieldCount++;
    return 0;
  }
  else if(registrationFieldCount==1){
    username=nextCommand;
    printf("password:");
    fflush(stdout);
    registrationFieldCount++;
    return 0;
  }
  else if(registrationFieldCount==2){
    password=nextCommand;
    if(uregister){
      printf("firstName:");
      fflush(stdout);
      registrationFieldCount++;
      return 0;
    }
  }
  else if(registrationFieldCount==3){
    firstName=nextCommand;
    printf("lastName:");
    fflush(stdout);
    registrationFieldCount++;
    return 0;
  }
  else if(registrationFieldCount==4){
    lastName=nextCommand;
    printf("email:");
    fflush(stdout);
    registrationFieldCount++;
    return 0;
  }
  else if(registrationFieldCount==5){
    email=nextCommand;
    request="Register::,";
    request+=username+comma+password+comma+firstName+comma+lastName+comma+email;
    registeredLogedin=true;
    Writen(sockfd,(void*)request.c_str(),request.length()+1);
    startudp+=SelfUsername();
    startudp+=colon+selfUdpAddress;
    udpsvlen=sizeof(udpservaddr);
    Sendto(client[2].fd,(void*)startudp.c_str(),startudp.length()+1,0,(SA *) &udpservaddr,udpsvlen);
    return 0;
  }
  request="Login::,";
  request+=username+comma+password;
  registeredLogedin=true;
  Writen(sockfd,(void*)request.c_str(),request.length()+1);
  startudp+=SelfUsername();
  startudp+=colon+selfUdpAddress;
  udpsvlen=sizeof(udpservaddr);
  Sendto(client[2].fd,(void*)startudp.c_str(),startudp.length()+1,0,(SA *) &udpservaddr,udpsvlen);
  return 0;
}
int ClientProcessor::InterfaceAddress(){
  struct ifaddrs *ifap, *ifa;
  struct sockaddr_in *sa;
  char *addr;
  string loopback="127.0.0.1";
  getifaddrs (&ifap);
  for (ifa = ifap; ifa; ifa = ifa->ifa_next) {
    if (ifa->ifa_addr && ifa->ifa_addr->sa_family==AF_INET) {
      sa = (struct sockaddr_in *) ifa->ifa_addr;
      interfaceAddress = inet_ntoa(sa->sin_addr);
      if(interfaceAddress !=loopback)
	break;
      //      printf("Interface: %s\tAddress: %s\n", ifa->ifa_name, addr);
    }
  }
  freeifaddrs(ifap);
  return 0;
}
int ClientProcessor::PortFromSocketFd(int socketFd,bool udp){
  
  struct sockaddr ownAddr;
  socklen_t len;
  len=sizeof(ownAddr);
  Getsockname(socketFd,(SA*)&ownAddr,&len);
  char* ownAddress=Sock_ntop((SA *) &ownAddr, len);
  if(udp)
    selfUdpAddress=ownAddress;
  else{
    selfTcpAddress=ownAddress;
    selfaddress=true;
  }
  return 0;
}
int ClientProcessor::ProcessUdp(){
  Recvfrom(client[2].fd,buf,MAXLINE, 0,(SA *) &udpservaddr, &udpsvlen);
  string packet=buf;
  if(packet.compare(0,6,"voice:")==0){
    vector<string> msgs;
    string msg=buf;
    boost::split(msgs,msg,boost::is_any_of(":"));
    //printf("playing packet %s\n",msgs[1].c_str());
    sp.PlaySoundPacket(msgs[2].c_str());
  }
  else{
    vector<string> msgs;
    string msg=buf;
    boost::split(msgs,msg,boost::is_any_of("\n"));
    char* svAddress=Sock_ntop((SA *) &udpservaddr, udpsvlen);
    printf("\n");
    printf(" \"%s\" received from %s\n",msgs[0].c_str(),svAddress);
    if(chat)
      printf("chat text:");
    else
      printf("next command:");
    fflush(NULL);
  }
  return 0;
}
