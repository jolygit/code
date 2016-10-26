#include "unp.h"
#include <stdio.h>
#include <stdlib.h>
#include <boost/algorithm/string.hpp>
#include <ctype.h>
//#include "tlpi_hdr.h"
#include        <vector>
#include        <string>
#define printable(ch) (isprint((unsigned char) ch) ? ch : '#')
using namespace std;
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
  ssize_t Readn(int, void*, size_t);
  ssize_t Read(int, void*, size_t);
  void	 Connect(int, const SA *, socklen_t);
  void	 Inet_pton(int, const char *, void *);
  void	 str_cli(FILE *, int);
  void	 Setsockopt(int, int, int, const void *, socklen_t);
  char	*Fgets(char *, int, FILE *);
  char	*Sock_ntop(const SA *, socklen_t);
  void	 Getsockname(int, SA *, socklen_t *);
}
class ClientProcessor{
 public:
int ConnectToFriend(string frienduid);
int TcpSimulteniousOpen(string frienduid);
int Commands(int sockfd);
int receive_int(int *num, int fd);
int ListFriends(char* req,int sockfd,vector<string>& strs);
int RequestFriend(string& req,int sockfd);
int Register(int sockfd,bool registering);
int PortFromSocketFd(int socketFd,string& port,string& addr);
int ClientServer(int ownsockfd);
 private:
 
};
