#include        "processor.h"

int Processor::Send_int(int num, int fd)
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
int Processor::GetFriends(string& friends,int i,string& value){
  string uid=clUID[i];
  string tmpval;
  db.RetreiveValueForUsernameByKey(uid.c_str(),"friends",tmpval);
  vector<string> vals;
  boost::split(vals,tmpval,boost::is_any_of("[]"));
  if(vals.size()<=1) // no requests
    return 0;
  tmpval=vals[1];
  boost::split(vals,tmpval,boost::is_any_of(","));
  for (size_t i = 0; i < vals.size(); i++){
    vector<string> reqs;
    boost::split(reqs,vals[i],boost::is_any_of("\""));
    string user=reqs[3];
    if(friends==string("friends")){
      value+=user;
      if(i<vals.size()-1)
	value+=",";
    }
    else{
    }
   
  }
  
  return 0;
}
int Processor::GetOnlineFriends(string& friends,string& uid,string& value){
 return 0;
}
int Processor::FriendRequest(string& fromUsername,string& toUsername){
  if(db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),NULL,NULL,NULL)){
    if(!db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),"friends","username",fromUsername.c_str()))
      if(!db.FindKeyValueInArrayOfDocument("username",toUsername.c_str(),"friendRequests","username",fromUsername.c_str()))
	db.AddArrayKeyValueToDocument("username",toUsername.c_str(),"friendRequests","username",fromUsername.c_str());
     printf ("request from %s to be friends with %s failed because either they are friends already or request is pending\n",fromUsername.c_str(),toUsername.c_str(),toUsername.c_str());
  }
  else{
    printf ("request from %s to be friends with %s failed because %s does not exist\n",fromUsername.c_str(),toUsername.c_str(),toUsername.c_str());
     return 1;
  }
  return 0;
}
int Processor::ProcessFriendRequests(string& clUID){
  string tmpval;
  db.RetreiveValueForUsernameByKey(clUID.c_str(),"friendRequests",tmpval);
  vector<string> vals;
  boost::split(vals,tmpval,boost::is_any_of("[]"));
  if(vals.size()<=1) // no requests
    return 0;
  tmpval=vals[1];
  boost::split(vals,tmpval,boost::is_any_of(","));
  for (size_t i = 0; i < vals.size(); i++){
    vector<string> reqs;
    boost::split(reqs,vals[i],boost::is_any_of("\""));
    string user=reqs[3];
    CreateFrinds(clUID,user);
    CreateFrinds(user,clUID);
  }
  return 0;
}
int Processor::CreateFrinds(string& clUID,string& user){
  if(!db.FindKeyValueInArrayOfDocument("username",clUID.c_str(),"friends","username",user.c_str())){
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
int Processor::Register(vector<string> &strs,string & msg,char* clAddress){
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
  if(db.FindKeyValueInArrayOfDocument("username",username.c_str(),"","",""))
    msg="user name already exist try different one.";
  else{
    db.AddKeyValueToNewDocument("username",username.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"password",password.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"firstName",firstName.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"lastName",lastName.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"email",email.c_str());
    db.AddKeyValueToExistingDocument("ip",ip.c_str(),"port",port.c_str());
  }
  return 0;
}
int Processor::Login(vector<string> &strs,string & msg,char* clAddress){
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
  printf("%s\n",password.c_str());
  if(!db.Find2KeyValuePair("username" , username.c_str(), "password" ,password.c_str())){//"username",username.c_str(),"password",password.c_str())){
    msg="user name and password you have provided do not match try again or register.";
    return 1;
  }
  else{
    db.AddKeyValueToExistingDocument("username",username.c_str(),"ip",ip.c_str());
    db.AddKeyValueToExistingDocument("username",username.c_str(),"port",port.c_str());
  }
  return 0;
}
int Processor::RegisterOrLogin(int sockfd,string& clUID,char const* buf,char* clAddress){
  string  str(buf);
  vector<string> strs;
  if(str.compare(0,11,"Register::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    string  uidstr(strs[1]);
    vector<string> uidstrs;
    boost::split(uidstrs,uidstr,boost::is_any_of("="));
    string  uid=uidstrs[1];
    string msg("ok");
    if(Register(strs,msg,clAddress)==0){
      printf("registration ok\n");
      clUID=uid;
    }
    Writen(sockfd,(void*)msg.c_str(),msg.length()+1);
    return 0;
  }
  else if(str.compare(0,8,"Login::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    if(strs.size()!=3)
      printf("wrong number of fields\n");
    string  uidstr(strs[1]);
    vector<string> uidstrs;
    boost::split(uidstrs,uidstr,boost::is_any_of("="));
    string  uid=uidstrs[1];
    string msg("ok");
    if(Login(strs,msg,clAddress)==0){
      printf("login ok\n");
      clUID=uid;
    }
    Writen(sockfd,(void*)msg.c_str(),msg.length()+1);
    return 0;
  }
  else{
    string msg("registration or login should start with Register::, or Login::, try again");
    char * cstr = new char [msg.length()+1];
    strcpy (cstr, msg.c_str());
    printf("%s\n", cstr);
    Writen(sockfd,cstr,msg.length()+1);
    return 1;
  }
}
