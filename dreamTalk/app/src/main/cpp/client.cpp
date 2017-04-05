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
    const char *address="192.168.1.117";
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
    clProcessor.udpselfaddr.sin_port = htons(0);
    clProcessor.InterfaceAddress();
    inet_pton(AF_INET,clProcessor.interfaceAddress.c_str(), &clProcessor.udpselfaddr.sin_addr);
    bind(udpfd, (SA *) &clProcessor.udpselfaddr, sizeof(clProcessor.udpselfaddr));
    clProcessor.PortFromSocketFd(udpfd,true);
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
    remove("/data/data/com.google.sample.echo/files/infile");
    int in=mkfifo("/data/data/com.google.sample.echo/files/infile", 0777);
    if(in)
        in=1;
    int fdi = open("/data/data/com.google.sample.echo/files/infile", O_RDONLY|O_NONBLOCK);
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
                   msg="chat:"+msg;
                    if(msg.size()>0) {//chat msg came
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
                        clProcessor.client[i].fd = -1;
                        clProcessor.clLogin[i]=false;
                        clProcessor.clUID[i]="";
                        return 0;
                    }
                    else if(tmp.compare(0,7,"Login::")==0 || tmp.compare(0,10,"Register::")==0){
                        clProcessor.Register(nextCommand);
                    }
                    else if(clProcessor.registeredLogedin && (tmp.compare(0,14,"friendaddress:")==0 || tmp.compare(0,10,"allfriends")==0 || tmp.compare(0,13,"onlinefriends")==0 || tmp.compare(0,12,"invitefriend")==0)) {
                            if(clProcessor.RequestToServer(nextCommand)){//in case of a wrong request function returns 1
                                string msg="wrong request"+nextCommand;//+"AbdulmanatKhabib:"
                                jstring jstr = (env)->NewStringUTF(msg.c_str());
                                (env)->CallVoidMethod(obj,ProcessRequest, jstr);//let main thread handler handle the chat msg i.e print it on the screen
                            }
                    }
                    else if(clProcessor.chat){//chat packet
                        sendto(clProcessor.client[2].fd,(void*)buf,n,0,(SA *) &clProcessor.fraddress,sizeof(clProcessor.fraddress));
                    }
                }
                else if (i == 1) { //server
                    if(!clProcessor.selfaddress)
                        clProcessor.PortFromSocketFd(sockfd,false);
                    if(!clProcessor.ResponseFromServer(buf)){//everyting exept udp hole puch is directed back to the main thread
                        //string msg="AbdulmanatKhabib:"+string(buf)+"AbdulmanatKhabib:";
                        jstring jstr = (env)->NewStringUTF(buf);
                        (env)->CallVoidMethod(obj,ProcessRequest, jstr);//let main thread handler handle the response i.e initiate a new request in case this one was good
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