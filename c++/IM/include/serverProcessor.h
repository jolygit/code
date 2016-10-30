#include	"unp.h"
#include	<limits.h>		/* for OPEN_MAX */
#include        "database.h"
#include        <boost/algorithm/string.hpp>
#include        <vector>
#include        <set>

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
  char	*Sock_ntop(const SA *, socklen_t);
}
using namespace std;


class ServerProcessor{
public:
  ServerProcessor(){db.ConnectToCollection("users","users");colon==":";}
  int RegisterOrLogin(int sockfd,string& clUID,char const* buf,char* clAddress);
  int InitiateTcpSimultOpen(int sockfd,string fruid);
  int Send_int(int num, int fd);
  int GetFriends(string& friends,int uid,string& value);
  int GetOnlineFriends(string& friends,string& uid,string& value);
  int FriendRequest(string& fromUsername,string& toUsername);
  int ProcessFriendRequests(string& clUID);
  int Register(vector<string> &strs,string & msg,char* clAddress);
  int Login(vector<string> &strs,string & msg,char* clAddress);
  int CreateFrinds(string& clUID,string& user);
  int GetAddress(string& fruid,string& address);
  bool SendResponse(int fd,string& command,string& msg);
  bool                    clLogin[myOPEN_MAX];
  string                  clUID[myOPEN_MAX];
 private:
  DataBase db;
  set<string>      userSet;
  string colon;
  char buffer[10];//holds integer
};
