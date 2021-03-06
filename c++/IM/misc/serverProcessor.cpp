#include        "serverProcessor.h"
#include        <sstream>
int ServerProcessor::AddUdpToDatabase(const char* clAddress,const char* buf){
  string address=clAddress;
  vector<string> udp_ip_port;
  boost::split(udp_ip_port,address,boost::is_any_of(":"));
  if(udp_ip_port.size()!=2){
    printf("address %s is wrong \n",address.c_str());
    return 1;
  }
  string msg=buf;
  vector<string> udp_username;
  string username,localUdpAddr;
  boost::split(udp_username,msg,boost::is_any_of(":"));
  if(udp_username.size()!=5){
    printf("message %s is wrong should be startudp:from:<username>\n",msg.c_str());
    return 1;
  }
  username=udp_username[2];
  localUdpAddr=udp_username[3]+colon+udp_username[4];
  if(!db.FindKeyValueInArrayOfDocument("username",username.c_str(),NULL,NULL,NULL)){
    printf("username %s does not exist this should never happen though\n",username.c_str());
    return 1;
  }
  else{
    db.AddKeyValueToExistingDocument("username",username.c_str(),"udpaddress",address.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"localudpaddress",localUdpAddr.c_str());
  }
  return 0;
}
int ServerProcessor::InitiateHolePunch(int sockfd,string fruid,bool udp,bool sameNet){
  int len=0;
  string msg,command("friendaddress");
  GetAddress(fruid,msg,udp,sameNet);
  msg+=colon;
  msg+=fruid;
  SendResponse(sockfd,command,msg);
  return 0;
}
bool ServerProcessor::SendResponse(int sockfd,string& command,string& msg)
{
  int l=msg.length()+1;
  stringstream response; 
  response << "version1.0:"<< l<<colon<<command<<colon<<msg;
  printf("sending %s\n",response.str().c_str());
  Writen(sockfd,(void*)response.str().c_str(),response.str().length()+1);
  return true;
}
int ServerProcessor::Send_int(int num, int fd)
{
    int32_t conv = htonl(num);
    char *data = (char*)&conv;
    int left = sizeof(conv);
    int rc;
    do {
        rc = write(fd, data, left);
        if (rc < 0) {
            if ((errno == EAGAIN) || (errno == EWOULDBLOCK)) {
                // use select() or epoll() to wait for the socket to be writable again
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
    return 0;
}
int ServerProcessor::GetFriends(string& friends,int i,string& value){
  userSet.clear();
  for (size_t ii = 0; ii < myOPEN_MAX; ii++){
    if(ii!=i && clUID[ii].size()>0)
      userSet.insert(clUID[ii]);
  }
  string uid=clUID[i];
  vector<string> tmpval;
  db.RetreiveValueForUsernameByKey(uid.c_str(),"friends",tmpval);
  string semicolon=";";
  for (size_t i = 0; i < tmpval.size(); i++){
    string user=tmpval[i];
    if(friends==string("allfriends")){//for each user send userid as well as first and last name 
      //get first name and last name for user
      string firstName;
      db.RetreiveValueForUsernameByKeySimple(user.c_str(),"firstName",firstName);
      string lastName;
      db.RetreiveValueForUsernameByKeySimple(user.c_str(),"lastName",lastName);
      if(value.size())
	value+=",";
      value+=user+semicolon+firstName+semicolon+lastName;
    }
    else{
      if(userSet.count(user)){
	if(value.size())
	  value+=",";
	value+=user;
      }
    }
  }
  return 0;
}

int ServerProcessor::FriendRequest(string& fromUsername,string& toUsername){
  if(db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),NULL,NULL,NULL)){
    if(!db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),"friends","username",fromUsername.c_str()))
      if(!db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),"friendRequests","username",fromUsername.c_str()))
	db.AddArrayKeyValueToDocument("username",toUsername.c_str(),"friendRequests","username",fromUsername.c_str());
     printf ("request filed successfully\n");
  }
  else{
    printf ("request from %s to be friends with %s failed because %s does not exist\n",fromUsername.c_str(),toUsername.c_str(),toUsername.c_str());
     return 1;
  }
  return 0;
}
int ServerProcessor::ProcessFriendRequests(string& clUID,int sockfd){
  vector<string> tmpval;
  if(!db.RetreiveValueForUsernameByKey(clUID.c_str(),"friendRequests",tmpval))
    return 0;
  string users;
  string comma=",";
  for (size_t i = 0; i < tmpval.size(); i++){
    if(i==0)
      users+=tmpval[i];
    else
      users+=(comma+tmpval[i]);
  }
  string requests="requests";
  SendResponse(sockfd,requests,users);
  return 0;
}
bool ServerProcessor::OnTheSameNet(string selfID,string fruid){
  string selfaddress,selfip;
  db.RetreiveValueForUsernameByKeySimple(selfID.c_str(),"udpaddress",selfaddress);
  vector<string> ip_port;
  boost::split(ip_port,selfaddress,boost::is_any_of(":"));
  selfip=ip_port[0];
  string fraddress,frip;
  db.RetreiveValueForUsernameByKeySimple(fruid.c_str(),"udpaddress",fraddress);
  boost::split(ip_port,fraddress,boost::is_any_of(":"));
  frip=ip_port[0];
  if(frip==selfip)
    return true;
  else
    return false;
}
int ServerProcessor::GetAddress(string& fruid,string& address,bool udp,bool sameNet){
  if(!udp && db.RetreiveValueForUsernameByKeySimple(fruid.c_str(),"address",address))
    return 0;
  else if(udp){
    string key;
    if(sameNet)
      key="localudpaddress";
    else
      key="udpaddress";
    db.RetreiveValueForUsernameByKeySimple(fruid.c_str(),key.c_str(),address);
    return 0;
  }
  else{
    printf("could not retreive address for uid %s\n",fruid.c_str());
    return 1;
  }
}
int ServerProcessor::CreateFriends(string& clUID,string& user,bool accept){
  if(!db.FindKeyValueInArrayOfDocument("username",clUID.c_str(),"friends","username",user.c_str())){
    if(accept)// request could be denied. In that case only remove it from the requests
      db.AddArrayKeyValueToDocument("username",clUID.c_str(),"friends","username",user.c_str());
    if(db.FindKeyValueInArrayOfDocument("username",clUID.c_str(),"friendRequests","username",user.c_str()))
      db.RemoveArrayKeyValueFromDocument("username",clUID.c_str(),"friendRequests","username",user.c_str());
  }
  else{
    printf("user %s is a friend already\n",user.c_str());
    if(db.FindKeyValueInArrayOfDocument("username",clUID.c_str(),"friendRequests","username",user.c_str()))
      db.RemoveArrayKeyValueFromDocument("username",clUID.c_str(),"friendRequests","username",user.c_str());
  }
  return 0;
}
int ServerProcessor::Register(vector<string> &strs,string & msg,char* clAddress){
  vector<string> ip_port;
  boost::split(ip_port,clAddress,boost::is_any_of(":"));
  string ip;
  string port;
  ip=ip_port[0];
  port=ip_port[1];
  string username;
  string password;
  string firstName;
  string lastName;
  string email;
  username=strs[1];
  password=strs[2];
  firstName=strs[3];
  lastName=strs[4];
  email=strs[5];
  printf("registering %s,%s\n",username.c_str(),password.c_str());
  if(db.FindKeyValueInArrayOfDocument("username",username.c_str(),NULL,NULL,NULL))
    msg="user name already exist try different one.";
  else{
    db.AddKeyValueToNewDocument("username",username.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"password",password.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"firstName",firstName.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"lastName",lastName.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"email",email.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"address",clAddress);
    // db.AddKeyValueToExistingDocument("ip",ip.c_str(),"port",port.c_str());
  }
  return 0;
}
int ServerProcessor::Login(vector<string> &strs,string & msg,char* clAddress){
  vector<string> ip_port;
  boost::split(ip_port,clAddress,boost::is_any_of(":"));
  string ip;
  string port;
  ip=ip_port[0];
  port=ip_port[1];
  string username;
  string password;
  username=strs[1];
  password=strs[2];
  boost::trim_right(password);
  //printf("%s\n",password.c_str());
  if(username.size()==0 || password.size()==0 || !db.Find2KeyValuePair("username" , username.c_str(), "password" ,password.c_str())){//"username",username.c_str(),"password",password.c_str())){
    msg="user name and password you have provided do not match try again or register.";
    return 1;
  }
  else{
    db.AddKeyValueToExistingDocument("username",username.c_str(),"address",clAddress);
    // db.AddKeyValueToExistingDocument("username",username.c_str(),"ip",ip.c_str());
    // db.AddKeyValueToExistingDocument("username",username.c_str(),"port",port.c_str());
  }
  return 0;
}
int ServerProcessor::RegisterOrLogin(int sockfd,string& clUID,char const* buf,char* clAddress){
  string  str(buf);
  vector<string> strs;
  string msg("ok"),command;
  int toreturn=1;
  if(str.compare(0,11,"Register::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    string  uidstr(strs[1]);
    if(Register(strs,msg,clAddress)==0){
      printf("registration ok\n");
      clUID=uidstr;
      command="registration";
      toreturn=0;
    }
  }
  else if(str.compare(0,8,"Login::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    if(strs.size()!=3)
      printf("wrong number of fields\n");
    string  uidstr(strs[1]);
    if(Login(strs,msg,clAddress)==0){
      printf("login ok\n");
      clUID=uidstr;
      command="login";
      toreturn=0;
    }
  }
  else{
    command="registration/login";
    msg=string("should start with Register::, or Login::, try again");
  }
  SendResponse(sockfd,command,msg);
  return toreturn;
}
