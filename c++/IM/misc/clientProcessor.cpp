#include "clientProcessor.h"

int ClientProcessor::RequestToServer(int sockfd){ //sockfd is server
  string nextCommand;
  vector<string> strs;
  cin >> nextCommand;
  string request("request:");
  if(nextCommand==allfriends || nextCommand==onlinefriends || nextCommand.compare(0,10,"friendaddress:")==0 || nextCommand.compare(0,13,"invitefriend:")==0){
    request+=nextCommand;
    Writen(sockfd,(void*)request.c_str(),request.length()+1);
  }
  else{
    printf("supported commands are:: allfriends;onlinefriends;invitefriend:<frienduid>;friendaddress:<frienduid>. Try again\n");
  }
  printf("next command:");
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
    printf("%s %s\n",strs[2].c_str(),strs[3].c_str()); // this should say weather request was ok or not 
  }
  else if(strs[2]==friendaddress){// version:response size:responseCommand:friendPort:friendIp:frUid
    printf("connecting to %s...\n",strs[5].c_str());
    TcpSimultaneousOpen(strs[3],strs[4]);
  }
  else{
    printf("supported commands are:allfriends;requestfriend;onlinefriends;friendaddress:<frienduid>. Try again\n");
  }
  printf("next command:");
  return 0;
}
int ClientProcessor::TcpSimultaneousOpen(string& friendPort,string& friendIp){
   struct sockaddr_in	fraddress, selfAddress;
  int			sockfd;
  sockfd = Socket(AF_INET, SOCK_STREAM, 0);
  int on=1;
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEADDR error");
  if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
    err_sys("setsockopt of SO_REUSEPORT error");
  bzero(&selfAddress, sizeof(selfAddress));
  selfAddress.sin_family      = AF_INET;
  Inet_pton(AF_INET, selfIp.c_str(), &selfAddress.sin_addr);
  selfAddress.sin_port        = htons(atoi(selfPort.c_str()));
  Bind(sockfd, (SA *) &selfAddress, sizeof(selfAddress));
  bzero(&fraddress, sizeof(fraddress));
  fraddress.sin_family      = AF_INET;
  Inet_pton(AF_INET, friendIp.c_str(), &fraddress.sin_addr);
  fraddress.sin_port        = htons(atoi(friendPort.c_str()));
  Connect(sockfd, (SA *) &fraddress, sizeof(fraddress));
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
int ClientProcessor::Register(int sockfd,bool registering){
  string request;
  string reply;
  string username;
  string password;
  string firstName;
  string lastName;
  string email;
  string comma=",";
  printf("username:");
  cin >> username;
  printf("password:");
  cin >> password;
  if(registering){
  printf("firstName:");
  cin >> firstName;
  printf("lastName: ");
  cin >> lastName;
  printf("email: ");
  cin >> email;
  request="Register::,";
  request+=username+comma+password+comma+firstName+comma+lastName+comma+email;
  }
  else{
    request="Login::,";
    request+=username+comma+password;
  }
  Writen(sockfd,(void*)request.c_str(),request.length()+1);
  return 0;
}
int ClientProcessor::PortFromSocketFd(int socketFd){
  struct sockaddr ownAddr;
  socklen_t len;
  len=sizeof(ownAddr);
  Getsockname(socketFd,(SA*)&ownAddr,&len);
  char* ownAddress=Sock_ntop((SA *) &ownAddr, len);
  vector<string> ip_port;
  boost::split(ip_port,ownAddress,boost::is_any_of(":"));
  selfIp=ip_port[0];
  selfPort=ip_port[1];
  selfaddress=true;
  //  printf("parsed ip and port of client %s %s\n",ip.c_str(),prt.c_str());
  return 0;
}
