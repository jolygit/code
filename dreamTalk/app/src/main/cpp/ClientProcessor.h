#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include        <vector>
#include        <string>
#include        <set>
#include        <map>

#include <iostream>
#include <arpa/inet.h>
#include	<sys/socket.h>	/* basic socket definitions */
#include	<sys/time.h>	/* timeval{} for select() */
#include	<time.h>		/* timespec{} for pselect() */
#include	<netinet/in.h>	/* sockaddr_in{} and other Internet defns */
#include	<arpa/inet.h>	/* inet(3) functions */
#include	<errno.h>
#include	<fcntl.h>		/* for nonblocking */
#include    <poll.h>
#include "audioProcessor.h"
#define	SA	struct sockaddr
#define	MAXLINE		4096
#define	BUFFSIZE	8192
#define	SERV_PORT	9877
#define printable(ch) (isprint((unsigned char) ch) ? ch : '#')
using namespace std;
class ClientProcessor{
public:
    ClientProcessor(bool _voiceConverstation=false){
        selfaddress=false;invitefriend="invitefriend";friendaddress="friendaddress";
        allfriends="allfriends";onlinefriends="onlinefriends";
        registration="registration";login="login";registrationlogin="registrationlogin";
        registeredLogedin=false;
        OutgoingvoiceCall="udp hole punch\n";//this will be overwritten
        registrationFieldCount=0;
        packetIndex=-1;
    }
    int InterfaceAddress();
    int ResponseFromServer(char* buf);
    int RequestToServer(string& nextCommand);
    int TcpSimultaneousOpen(string& friendPort,string& friendIp,string& fruid);
    int UDPHolePunch(string& friendPort,string& friendIp,string uid);
    string ProcessUdp();
    int Receive_int(int *num, int fd);
    int Register(string& nextCommand);
    int PortFromSocketFd(int socketFd,bool udp);
    int StartSendingSound();
    int StartVoiceConversation();
    int StopVoiceConversation();
    void split(std::vector<string> &strs,char* str,const char* delim);
    static void* Sound_wrapper(void* object)
    {
        reinterpret_cast<ClientProcessor*>(object)->StartSendingSound();
        return 0;
    }
    string SelfUsername(){return username;}
    static const int  myOPEN_MAX = 10000;
    bool                    clLogin[myOPEN_MAX];
    string                  clUID[myOPEN_MAX];
    struct pollfd	        client[myOPEN_MAX];
    bool selfaddress;
    bool registeredLogedin;
    int maxi;
    struct sockaddr_in	udpservaddr,udpselfaddr;
    socklen_t		udpsvlen;
    bool                  chat=false;
    map<string,struct sockaddr_in>	fraddresses;
    set<string>                     udpHolePunchedForThisUid;
    string                OutgoingvoiceCall;
    string                interfaceAddress;
private:
    struct sockaddr_in peerAddress;
    AudioProcessor audio_Proc;
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
    int packetIndex;
    string startudp="startudp:from:";
    char			buf[MAXLINE];
    pthread_t	tid;
};