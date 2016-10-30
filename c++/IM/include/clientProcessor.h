#include "unp.h"
#include <stdio.h>
#include <stdlib.h>
#include <boost/algorithm/string.hpp>
#include <ctype.h>
//#include "tlpi_hdr.h"
#include        <vector>
#include        <string>
#include <iostream>
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
  ClientProcessor(){
    selfaddress=false;invitefriend="invitefriend";friendaddress="friendaddress";
    allfriends="allfriends";onlinefriends="onlinefriends";
    registration="registration";login="login";registrationlogin="registrationlogin";
  }
  int ResponseFromServer(char* buf);
  int RequestToServer(int sockfd);
  int TcpSimultaneousOpen(string& friendPort,string& friendIp);
  int Receive_int(int *num, int fd);
  int Register(int sockfd,bool registering);
  int PortFromSocketFd(int socketFd);
  bool                    clLogin[myOPEN_MAX];
  string                  clUID[myOPEN_MAX];
  bool selfaddress;
 private:
  string selfPort,selfIp;
  string invitefriend,friendaddress;
  string allfriends,onlinefriends;
  string registration,login,registrationlogin;
};
