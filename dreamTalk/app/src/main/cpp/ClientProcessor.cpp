//
// Created by alex on 3/20/17.
//

#include "ClientProcessor.h"
ssize_t						/* Write "n" bytes to a descriptor. */
writen(int fd, const void *vptr, size_t n)
{
    size_t		nleft;
    ssize_t		nwritten;
    const char	*ptr;

    ptr = (const char*)vptr;//ALERT
    nleft = n;
    while (nleft > 0) {
        if ( (nwritten = write(fd, ptr, nleft)) <= 0) {
            if (nwritten < 0 && errno == EINTR)
                nwritten = 0;		/* and call write() again */
            else
                return(-1);			/* error */
        }

        nleft -= nwritten;
        ptr   += nwritten;
    }
    return(n);
}
char *
sock_ntop(const struct sockaddr *sa, socklen_t salen)
{
    char		portstr[8];
    static char str[128];		/* Unix domain is largest */

    switch (sa->sa_family) {
        case AF_INET: {
            struct sockaddr_in	*sin = (struct sockaddr_in *) sa;

            if (inet_ntop(AF_INET, &sin->sin_addr, str, sizeof(str)) == NULL)
                return(NULL);
            if (ntohs(sin->sin_port) != 0) {
                snprintf(portstr, sizeof(portstr), ":%d", ntohs(sin->sin_port));
                strcat(str, portstr);
            }
            return(str);
        }
/* end sock_ntop */

#ifdef	IPV6
        case AF_INET6: {
		struct sockaddr_in6	*sin6 = (struct sockaddr_in6 *) sa;

		str[0] = '[';
		if (inet_ntop(AF_INET6, &sin6->sin6_addr, str + 1, sizeof(str) - 1) == NULL)
			return(NULL);
		if (ntohs(sin6->sin6_port) != 0) {
			snprintf(portstr, sizeof(portstr), "]:%d", ntohs(sin6->sin6_port));
			strcat(str, portstr);
			return(str);
		}
		return (str + 1);
	}
#endif
        default:
            snprintf(str, sizeof(str), "sock_ntop: unknown AF_xxx: %d, len %d",
                     sa->sa_family, salen);
            return(str);
    }
    return (NULL);
}
void ClientProcessor::split(std::vector<string> &strs,char* str,const char* delim) {
    char * pch;
    pch = strtok(str,delim);
    while (pch != NULL)
    {
        strs.push_back(std::string(pch));
        pch = strtok (NULL,delim);
    }
}
int ClientProcessor::RequestToServer(string& nextCommand){ //sockfd is server
    string request("request:");
    int sockfd=client[1].fd;
    if(nextCommand==allfriends || nextCommand==onlinefriends || nextCommand.compare(0,14,"friendaddress:")==0 || nextCommand.compare(0,13,"invitefriend:")==0 || nextCommand.compare(0,7,"accept:")==0 || nextCommand.compare(0,7,"reject:")==0){
        request+=nextCommand;
        writen(sockfd,(void*)request.c_str(),request.length()+1);
        if(nextCommand.compare(0,14,"friendaddress:")==0){
            chat=true;
        }
    }
    else{
        //printf("AbdulmanatKhabib:wrong request %sAbdulmanatKhabib:",nextCommand.c_str());
        //printf("supported commands are:: allfriends;onlinefriends;invitefriend:<frienduid>;friendaddress:<frienduid>. Try again\n");
        //printf("next command:");
        //fflush(stdout);
        return 1;
    }
    return 0;
}
int ClientProcessor::ResponseFromServer(char* buf){
    string response=buf;
    string command;
    vector<string> strs;
    split(strs,(char*)response.c_str(),(const char*)":");
    if(strs[2]==allfriends || strs[2]==onlinefriends || strs[2]==invitefriend || strs[2]==registration || strs[2]==login || strs[2]==registrationlogin){ // version:response size:responseCommand:friend1,friend2,...
        //printf("AbdulmanatKhabib:%sAbdulmanatKhabib:\n",response.c_str());// this is done cos there is a bug that sometimes returns garbage at the begining of the sent string
        registeredLogedin=true;
    }
        //sometimes udp connection to server does not go through and server does not have the udp address and port. IN that case strs.size() will be less then 6. Probably need to make udp connection to server reliable
    else if(strs[2]==friendaddress && strs.size()==6){// version:response size:responseCommand:friendIp:friendPort:frUid
        // printf("\n connecting to %s...\n",strs[5].c_str());
        // fflush(NULL);
        UDPHolePunch(strs[4],strs[3],strs[5]);
        //if(true)//record
        //   pthread_create(&tid, NULL, Sound_wrapper, this);//separate thread reads sound from mic and sends to udp socket
        //int sockfd=TcpSimultaneousOpen(strs[4],strs[3],strs[5]);
        return 1;
    }
    else{
        // printf("AbdulmanatKhabib:%s AbdulmanatKhabib:\n",response.c_str());
    }
    return 0;
}
int ClientProcessor::StartSendingSound(){
    audio_Proc.startRecording(client[2].fd,peerAddress);
    return 0;
}
int ClientProcessor::StartVoiceConversation() {
    audio_Proc.SetUp();
    sleep(1);
    int result=pthread_create(&tid, NULL, Sound_wrapper, this);
    return result;
}
int ClientProcessor::UDPHolePunch(string& friendPort,string& friendIp,string uid){

    string msg=OutgoingvoiceCall;//"udp hole punch\n";
    bzero(&peerAddress, sizeof(peerAddress));
    peerAddress.sin_family = AF_INET;
    peerAddress.sin_port = htons(atoi(friendPort.c_str()));
    inet_pton(AF_INET, friendIp.c_str(), &peerAddress.sin_addr);
    fraddresses[uid]=peerAddress;
    udpHolePunchedForThisUid.insert(uid);
    sendto(client[2].fd,(void*)msg.c_str(),msg.length(),0,(SA *) &peerAddress,sizeof(peerAddress));
    OutgoingvoiceCall="udp hole punch\n";//reset to defauld
    //chat=true;
    return 0;
}
int ClientProcessor::TcpSimultaneousOpen(string& friendPort,string& friendIp,string& fruid){
    // struct sockaddr_in	fraddress, selfAddress;
    // int			sockfd;
    // sockfd = Socket(AF_INET, SOCK_STREAM, 0);
    // int on=1;
    // if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof (on)) < 0)
    //   err_sys("setsockopt of SO_REUSEADDR error");
    // if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEPORT, &on, sizeof (on)) < 0)
    //   err_sys("setsockopt of SO_REUSEPORT error");
    // bzero(&selfAddress, sizeof(selfAddress));
    // selfAddress.sin_family      = AF_INET;
    // Inet_pton(AF_INET, selfTcpIp.c_str(), &selfAddress.sin_addr);
    // selfAddress.sin_port        = htons(atoi(selfTcpPort.c_str()));
    // Bind(sockfd, (SA *) &selfAddress, sizeof(selfAddress));
    // bzero(&fraddress, sizeof(fraddress));
    // fraddress.sin_family      = AF_INET;
    // Inet_pton(AF_INET, friendIp.c_str(), &fraddress.sin_addr);
    // fraddress.sin_port        = htons(atoi(friendPort.c_str()));
    // Connect(sockfd, (SA *) &fraddress, sizeof(fraddress));
    // int ii;
    // for (ii = 1; ii < myOPEN_MAX; ii++)
    //   if (client[ii].fd < 0) {
    //     client[ii].fd = sockfd;	/* save descriptor */
    //     clLogin[ii]=true;
    //     clUID[ii]=fruid;
    //     break;
    //   }
    // if (ii == myOPEN_MAX)
    //   err_quit("too many clients");
    // client[ii].events = POLLIN;
    // if (ii > maxi)
    //   maxi = ii;
    printf("sucessfully connected to: %s\n",fruid.c_str());
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
int ClientProcessor::Register(string& nextCommand){
    string str=nextCommand;
    vector<string> msgs;
    split(msgs,(char*)str.c_str(),(const char*)",");
    if(msgs.size()>2)
        username=msgs[1];
    else
    {
        // printf("could not retreive username from %s",nextCommand.c_str());
        return 1;
    }
    int sockfd=client[1].fd;
    writen(sockfd,(void*)nextCommand.c_str(),nextCommand.length()+1);
    startudp="startudp:from:";
    startudp+=SelfUsername();
    startudp+=colon+selfUdpAddress;
    udpsvlen=sizeof(udpservaddr);
    sendto(client[2].fd,(void*)startudp.c_str(),startudp.length()+1,0,(SA *) &udpservaddr,udpsvlen);
    return 0;
}
int ClientProcessor::InterfaceAddress(){
    /*struct ifaddrs *ifap, *ifa;
    struct sockaddr_in *sa;
    char *addr;
    string loopback="127.0.0.1";
    getifaddrs (&ifap);
    for (ifa = ifap; ifa; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr && ifa->ifa_addr->sa_family==AF_INET) {
            sa = (struct sockaddr_in *) ifa->ifa_addr;
            interfaceAddress = inet_ntoa(sa->sin_addr);
            if(interfaceAddress !=loopback)
                break;
            //      printf("Interface: %s\tAddress: %s\n", ifa->ifa_name, addr);
        }
    }
    freeifaddrs(ifap);*/
    vector<string> msgs;
    string buf=selfTcpAddress;
    split(msgs,(char*)buf.c_str(),(const char*)":");
    if(msgs.size()==2)
        interfaceAddress=msgs[0]; //retreiving interface address without port need for udp socket
    else{
        printf("could not retreve interface address");
        return 1;
    }
    return 0;
}
int ClientProcessor::PortFromSocketFd(int socketFd,bool udp){

    struct sockaddr ownAddr;
    socklen_t len;
    len=sizeof(ownAddr);
    int rt=getsockname(socketFd,(SA*)&ownAddr,&len);
    printf("%d",rt);
    char* ownAddress=sock_ntop((SA *) &ownAddr, len);
    if(udp)
        selfUdpAddress=ownAddress;//port number is not used but server expects this format
    else{
        selfTcpAddress=ownAddress;
        selfaddress=true;
    }
    return 0;
}
typedef struct  WAV_HEADER
{
    /* RIFF Chunk Descriptor */
    uint8_t         RIFF[4];        // RIFF Header Magic header
    uint32_t        ChunkSize;      // RIFF Chunk Size
    uint8_t         WAVE[4];        // WAVE Header
    /* "fmt" sub-chunk */
    uint8_t         fmt[4];         // FMT header
    uint32_t        Subchunk1Size;  // Size of the fmt chunk
    uint16_t        AudioFormat;    // Audio format 1=PCM,6=mulaw,7=alaw,     257=IBM Mu-Law, 258=IBM A-Law, 259=ADPCM
    uint16_t        NumOfChan;      // Number of channels 1=Mono 2=Sterio
    uint32_t        SamplesPerSec;  // Sampling Frequency in Hz
    uint32_t        bytesPerSec;    // bytes per second
    uint16_t        blockAlign;     // 2=16-bit mono, 4=16-bit stereo
    uint16_t        bitsPerSample;  // Number of bits per sample
    /* "data" sub-chunk */
    uint8_t         Subchunk2ID[4]; // "data"  string
    uint32_t        Subchunk2Size;  // Sampled data length
} wav_hdr;
//static char recording[4096*536];
bool start=true;
#define RECORDER_FRAMES (800)
string ClientProcessor::ProcessUdp() {
    int leng=recvfrom(client[2].fd,buf,MAXLINE, 0,(SA *) &udpservaddr, &udpsvlen);
    string packet=buf;
    int l=packet.size();
    if(leng>100){//packet.compare(0,6,"voice:")==0){
        audio_Proc.Play((short*)buf,0); //buf is chat* i.e l is in bytes but in short we have 2 bytes i.e /2
        //vector<string> msgs;
        //split(msgs,buf,(const char*)":");
        //packet.clear();//return zero str
        bzero(buf,leng);
        return "";
    }
    else if(packet.compare(0,17,"IncomingVoiceFrom")==0 || packet.compare(0,17,"CanceledVoiceFrom")==0 || packet.compare(0,17,"RejectedVoiceFrom")==0 || packet.compare(0,17,"AcceptedVoiceFrom")==0 || packet.compare(0,15,"HangupVoiceFrom")==0){
        if(packet.compare(0,17,"AcceptedVoiceFrom")==0){//when we got accepted we start recording sound and sending it over udp
            StartVoiceConversation();
        }
        else if(packet.compare(0,15,"HangupVoiceFrom")==0)
            StopVoiceConversation();
        bzero(buf, leng);
        return packet;
    }
    bzero(buf, l);//erasing previous data
    return packet;//if it is a chat message return it else return empty str*/
}
int ClientProcessor::StopVoiceConversation(){
    //audio_Proc.stopRecording();
    audio_Proc.shutdown();
    int result=pthread_kill(tid,9);
    return result;
}