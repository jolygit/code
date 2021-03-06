#include "unp.h"
#include <stdio.h>
#include <stdlib.h>
#include <boost/algorithm/string.hpp>
#include <ctype.h>
//#include "tlpi_hdr.h"
#include        <vector>
#include        <string>
#include <iostream>
#include <arpa/inet.h>
#include <ifaddrs.h>
#include "soundProcessor.h"
#define printable(ch) (isprint((unsigned char) ch) ? ch : '#')
using namespace std;
extern "C"{
  void	 Sendto(int, const void *, size_t, int, const SA *, socklen_t);
  ssize_t	 Recvfrom(int, void *, size_t, int, SA *, socklen_t *);
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
  ClientProcessor(bool _record=true){
    selfaddress=false;invitefriend="invitefriend";friendaddress="friendaddress";
    allfriends="allfriends";onlinefriends="onlinefriends";
    registration="registration";login="login";registrationlogin="registrationlogin";
    registeredLogedin=false;
    record=_record;
    registrationFieldCount=0;
    sp.SetUpPlayer();
  }
  int InterfaceAddress();
  int ResponseFromServer(char* buf);
  int RequestToServer(string& nextCommand);
  int TcpSimultaneousOpen(string& friendPort,string& friendIp,string& fruid);
  int UDPHolePunch(string& friendPort,string& friendIp);
  int ProcessUdp();
  int Receive_int(int *num, int fd);
  int Register(string& nextCommand);
  int PortFromSocketFd(int socketFd,bool udp);
  int StartSendingSound();
  static void* Sound_wrapper(void* object)
  {
    reinterpret_cast<ClientProcessor*>(object)->StartSendingSound();
    return 0;
  }
  string SelfUsername(){return username;}
  bool                    clLogin[myOPEN_MAX];
  string                  clUID[myOPEN_MAX];
  struct pollfd	          client[myOPEN_MAX];
  bool selfaddress;
  bool registeredLogedin;
  int maxi;
  struct sockaddr_in	udpservaddr,udpselfaddr;
  socklen_t		udpsvlen;
  bool                  chat=false;
  struct sockaddr_in	fraddress;
  string                interfaceAddress;
 private:
  SoundProcessor sp;
  string selfTcpAddress,selfUdpAddress;
  string invitefriend,friendaddress;
  string allfriends,onlinefriends;
  string registration,login,registrationlogin;
  string username;
  string password;
  string firstName;
  string lastName;
  string email;
  string comma=",";
  string colon=":";
  int    registrationFieldCount;
  bool   uregister;
  string startudp="startudp:from:";
  char			buf[MAXLINE];
  pthread_t	tid;
  bool  record;
};
