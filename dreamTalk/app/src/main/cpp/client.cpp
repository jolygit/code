//
// Created by alex on 3/20/17.
//
#include	<sys/types.h>	/* basic system data types */
#include	<sys/socket.h>	/* basic socket definitions */
#include	<sys/time.h>	/* timeval{} for select() */
#include	<time.h>		/* timespec{} for pselect() */
#include	<netinet/in.h>	/* sockaddr_in{} and other Internet defns */
#include	<arpa/inet.h>	/* inet(3) functions */
#include	<errno.h>
#include	<fcntl.h>		/* for nonblocking */
#include	<netdb.h>
#include	<signal.h>
#include	<stdio.h>
#include	<stdlib.h>
#include	<string.h>
#include	<sys/stat.h>	/* for S_xxx file mode constants */
#include	<sys/uio.h>		/* for iovec{} and readv/writev */
#include	<unistd.h>
#include	<sys/wait.h>
#include	<sys/un.h>		/* for Unix domain sockets */
#include    <jni.h>
#include    <string>
#include    <poll.h>
#include    "ClientProcessor.h"
#define	MAXLINE		4096
#define	BUFFSIZE	8192
#define	SERV_PORT	9877
#define INFTIM      (-1)
#define	SA	struct sockaddr

using namespace::std;
extern "C" {
JNIEXPORT int JNICALL
Java_com_google_sample_echo_MainActivity_client(JNIEnv * env , jobject obj) ;//jclass
}
JNIEXPORT int JNICALL
Java_com_google_sample_echo_MainActivity_client(JNIEnv * env , jobject obj) {//jclass
    bool uregister=false;
    const char *address="67.80.234.215";//192.168.1.117";////"
    int			sockfd,udpfd,serversocketfd;
    struct sockaddr_in	servaddr;
    socklen_t		svlen;
    ClientProcessor clProcessor(uregister);
    int on=1,nready,n;
    char				buf[MAXLINE];
    //create tcp socket
    serversocketfd = socket(AF_INET, SOCK_STREAM, 0);
    if (setsockopt(serversocketfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
        //err_sys("setsockopt of SO_REUSEADDR error");
    if (setsockopt(serversocketfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
        //err_sys("setsockopt of SO_REUSEPORT error");
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(SERV_PORT);
    inet_pton(AF_INET, address, &servaddr.sin_addr);
    connect(serversocketfd, (SA *) &servaddr, sizeof(servaddr));
    clProcessor.PortFromSocketFd(serversocketfd,false);
    // Create udp socket
    udpfd = socket(AF_INET, SOCK_DGRAM, 0);
    bzero(&clProcessor.udpselfaddr, sizeof(clProcessor.udpselfaddr));
    clProcessor.udpselfaddr.sin_family = AF_INET;
    clProcessor.udpselfaddr.sin_port = htons(SERV_PORT);
    clProcessor.InterfaceAddress();
    const char* selfAddr=clProcessor.interfaceAddress.c_str();
    inet_pton(AF_INET,selfAddr, &clProcessor.udpselfaddr.sin_addr);
    while(::bind(udpfd, (SA *) &clProcessor.udpselfaddr, sizeof(clProcessor.udpselfaddr)) ==-1) {//:: is important here because of the using namespace std, without :: wrong bind was being called
        sleep(1);// poprt SERV_PORT could be used just a while ago so let system make it available again.
    }
    clProcessor.PortFromSocketFd(udpfd,true);
    //create server udp address
    bzero(&clProcessor.udpservaddr, sizeof(clProcessor.udpservaddr));
    clProcessor.udpservaddr.sin_family = AF_INET;
    clProcessor.udpservaddr.sin_port = htons(SERV_PORT);
    inet_pton(AF_INET, address, &clProcessor.udpservaddr.sin_addr);
    svlen = sizeof(clProcessor.udpservaddr);
    //preparing java call
    jclass cls=env->GetObjectClass(obj);
    //jclass cls = (env)->FindClass("com/google/sample/echo/MainActivity");//make sure to change the full path if you change it in java
    jmethodID ProcessRequest = (env)->GetMethodID(cls, "ProcessRequest", "(Ljava/lang/String;)V");
// create the pipe for stdin
    remove("/data/data/com.google.sample.echo/infile");//files/infile
    int in=mkfifo("/data/data/com.google.sample.echo/infile", 0777);
    if(in)
        in=1;
    int fdi = open("/data/data/com.google.sample.echo/infile", O_RDONLY|O_NONBLOCK);
    dup2(fdi, 0);
    // create the pipe for stdout
    /*setbuf(stdout, NULL);
    remove("/data/data/com.google.sample.echo/files/outfile");
    mkfifo("/data/data/com.google.sample.echo/files/outfile", 0777);
    int fdo = open("/data/data/com.google.sample.echo/files/outfile", O_WRONLY);
    int newfd=dup2(fdo, 1);
    if(newfd==1)
        newfd=1;*/
    clProcessor.client[0].fd = fileno(stdin);
    clProcessor.client[0].events = POLLIN;
    clProcessor.clLogin[0]=true;
    clProcessor.clUID[0]="stdInput";
    //tcp
    clProcessor.client[1].fd = serversocketfd;
    clProcessor.client[1].events = POLLIN;
    clProcessor.clLogin[1]=true;
    clProcessor.clUID[1]="server_tcp";
    //udp
    clProcessor.client[2].fd = udpfd;
    clProcessor.client[2].events = POLLIN;
    clProcessor.clLogin[2]=true;
    clProcessor.PortFromSocketFd(udpfd,true);
    clProcessor.clUID[2]="server_udp";
    //printf("next command:");
    //fflush(NULL);
    static const int  myOPEN_MAX = 10000;
    for (int i = 3; i < myOPEN_MAX; i++){
        clProcessor.client[i].fd = -1;		/* -1 indicates available entry */
        clProcessor.clLogin[i]=false;
        clProcessor.clUID[i]="";
    }
    clProcessor.maxi = 2;					/* max index into client[] array */


    string exitstr="exit";
    for ( ; ; ) {
        //continue;
        nready = poll(clProcessor.client, clProcessor.maxi+1, INFTIM);
        for (int i = 0; i <= clProcessor.maxi; i++) {	/* check all clients for data */
            if ( (sockfd = clProcessor.client[i].fd) < 0)
                continue;
            if (clProcessor.client[i].revents & (POLLIN | POLLERR)) {
                if(i==2){// incoming udp data
                    string msg=clProcessor.ProcessUdp();
                    if(msg.size()>0) {//chat msg came of the form chat:<sourceUserName>:chatmsg
                        jstring jstr = (env)->NewStringUTF(msg.c_str());
                        (env)->CallVoidMethod(obj,ProcessRequest, jstr);//let main thread handler handle the chat msg i.e print it on the screen
                    }
                }
                else if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
                    if (errno == ECONNRESET) {
                        /*4connection reset by client */
                        printf("clProcessor.client[%d] aborted connection\n", i);
                        close(sockfd);
                        clProcessor.client[i].fd = -1;
                        clProcessor.clLogin[i]=false;
                        clProcessor.clUID[i]="";
                    } else{}
                        //err_sys("read error");
                } else if (n == 0) {
                    /*4connection closed by client */
                    printf("clProcessor.client[%d] closed connection\n", i);
                    close(sockfd);
                    clProcessor.client[i].fd = -1;
                    clProcessor.clLogin[i]=false;
                    clProcessor.clUID[i]="";
                }
                else if (i == 0) { //stdin
                    string tmp=buf;
                    string nextCommand=tmp.substr(0,n-1);

                    if(tmp==exitstr){//need to finish the thread
                        close(serversocketfd);
                        close(udpfd);
                        clProcessor.client[i].fd = -1;
                        clProcessor.clLogin[i]=false;
                        clProcessor.clUID[i]="";
                        return 0;
                    }
                    else if(tmp.compare(0,17,"IncomingVoiceFrom")==0 || tmp.compare(0,17,"CanceledVoiceFrom")==0 || tmp.compare(0,17,"RejectedVoiceFrom")==0 || tmp.compare(0,17,"AcceptedVoiceFrom")==0 || tmp.compare(0,15,"HangupVoiceFrom")==0){
                        clProcessor.OutgoingvoiceCall=tmp;
                        vector<string> strs;
                        string colon=":";
                        clProcessor.split(strs,(char*)tmp.c_str(),(const char*)":");
                        string msg="";
                        for(int u=0;u<strs.size()-1;u++) //all but the first and last token is the message. since users could have used : in their message we have to have the loop below
                            if(u!=0)
                                msg+=colon+strs[u];
                            else
                                msg=strs[u];
                        string UseridCombo=strs[strs.size()-1];//last token is combination of userIDs to whom to send message
                        vector<string> userids;
                        clProcessor.split(userids,(char*)UseridCombo.c_str(),(const char*)"_");// this token is chosen to reduce the chance that it will be part of the chat conversation
                        for(int u=0;u<userids.size();u++) {// sending to all the destinations
                            string uid=userids[u];
                            if (clProcessor.udpHolePunchedForThisUid.find(uid) !=clProcessor.udpHolePunchedForThisUid.end()) { //make sure udp hole is punched for uid
                                  struct sockaddr_in fraddress = clProcessor.fraddresses[uid];
                                    sendto(clProcessor.client[2].fd, (void *) msg.c_str(),msg.size(), 0, (SA *) &fraddress, sizeof(fraddress));
                                clProcessor.OutgoingvoiceCall="udp hole punch\n";//resettting it to the defauld
                            }
                        }
                        if(tmp.compare(0,17,"AcceptedVoiceFrom")==0)//when we accept we start recording sound and sending it over udp
                            clProcessor.StartVoiceConversation();
                        if(tmp.compare(0,15,"HangupVoiceFrom")==0)
                            clProcessor.StopVoiceConversation();
                    }
                    else if(tmp.compare(0,7,"Login::")==0 || tmp.compare(0,10,"Register::")==0){
                        clProcessor.Register(nextCommand);
                    }
                    else if(clProcessor.registeredLogedin && (tmp.compare(0,14,"friendaddress:")==0 || tmp.compare(0,10,"allfriends")==0 || tmp.compare(0,13,"onlinefriends")==0 || tmp.compare(0,12,"invitefriend")==0
                                                              || tmp.compare(0,7,"accept:")==0 || tmp.compare(0,7,"reject:")==0)) {
                            if(clProcessor.RequestToServer(nextCommand)){//in case of a wrong request function returns 1
                                string msg="wrong request"+nextCommand;//+"AbdulmanatKhabib:"
                                jstring jstr = (env)->NewStringUTF(msg.c_str());
                                (env)->CallVoidMethod(obj,ProcessRequest, jstr);//let main thread handler handle the chat msg i.e print it on the screen
                            }
                    }
                    else if(tmp.compare(0,5,"chat:")==0){//chat packet it has the format chat:<selfusername>:chatmsg:<destination userNames separated by eabdulmanatkhabibgivivajaalko>
                        vector<string> strs;
                        string colon=":";
                        clProcessor.split(strs,(char*)tmp.c_str(),(const char*)":");
                        string msg="";
                        for(int u=0;u<strs.size()-1;u++) //all but the first and last token is the message. since users could have used : in their message we have to have the loop below
                            if(u!=0)
                                msg+=colon+strs[u];
                            else
                                msg=strs[u];
                        string UseridCombo=strs[strs.size()-1];//last token is combination of userIDs to whom to send message
                        vector<string> userids;
                        clProcessor.split(userids,(char*)UseridCombo.c_str(),(const char*)"_");// this token is chosen to reduce the chance that it will be part of the chat conversation
                        for(int u=0;u<userids.size();u++) {// sending to all the destinations
                            string uid=userids[u];
                            if(clProcessor.udpHolePunchedForThisUid.find(uid) != clProcessor.udpHolePunchedForThisUid.end()) { //make sure udp hole is punched for uid
                                struct sockaddr_in fraddress=clProcessor.fraddresses[uid];
                                sendto(clProcessor.client[2].fd, (void *) msg.c_str(), msg.size(),0, (SA *) &fraddress,sizeof(fraddress));
                            }
                        }
                    }
                }
                else if (i == 1) { //server
                    if(!clProcessor.selfaddress)
                        clProcessor.PortFromSocketFd(sockfd,false);
                    //sometimes buffer containes the following  version1.0:3:login:ok'\0'version1.0:3:requests:la  i.e two server responses in one buffer. For that reason we need to handle it
                    int numRead=0;
                    while(true) {
                        string msg = string((buf+numRead));//
                        numRead+=msg.size();
                        if (!clProcessor.ResponseFromServer(buf)) {//everyting exept udp hole puch is directed back to the main thread
                            jstring jstr = (env)->NewStringUTF(msg.c_str());
                            (env)->CallVoidMethod(obj, ProcessRequest,
                                                  jstr);//let main thread handler handle the response i.e initiate a new request in case this one was good
                        }
                        if(numRead==(n-1))
                            break;
                        else
                            numRead++;//add \0 to the count
                    }

                }
                else{

                }
                bzero(buf,n);//erasing previous data
                if (--nready <= 0)
                    break;				/* no more readable descriptors */
            }
        }
    }
}