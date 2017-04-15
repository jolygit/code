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
    else if(strs[2]==friendaddress){// version:response size:responseCommand:friendIp:friendPort:frUid
        // printf("\n connecting to %s...\n",strs[5].c_str());
        // fflush(NULL);
        UDPHolePunch(strs[4],strs[3],strs[5]);
        if(record)
            pthread_create(&tid, NULL, Sound_wrapper, this);//separate thread reads sound from mic and sends to udp socket
        //int sockfd=TcpSimultaneousOpen(strs[4],strs[3],strs[5]);
        return 1;
    }
    else{
       // printf("AbdulmanatKhabib:%s AbdulmanatKhabib:\n",response.c_str());
    }
    //printf("next command:");
    //fflush(NULL);
    return 0;
}
int ClientProcessor::StartSendingSound(){
    //sp.RecordAndSend(client[2].fd,fraddress);
    return 0;
}
int ClientProcessor::UDPHolePunch(string& friendPort,string& friendIp,string uid){

    string msg="udp hole punch\n";
    struct sockaddr_in fraddress;
    bzero(&fraddress, sizeof(fraddress));
    fraddress.sin_family = AF_INET;
    fraddress.sin_port = htons(atoi(friendPort.c_str()));
    inet_pton(AF_INET, friendIp.c_str(), &fraddress.sin_addr);
    fraddresses[uid]=fraddress;
    udpHolePunchedForThisUid.insert(uid);
    sendto(client[2].fd,(void*)msg.c_str(),msg.length(),0,(SA *) &fraddress,sizeof(fraddress));
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
string ClientProcessor::ProcessUdp(){
    recvfrom(client[2].fd,buf,MAXLINE, 0,(SA *) &udpservaddr, &udpsvlen);
    string packet=buf;
    int l=packet.size();
    if(packet.compare(0,6,"voice:")==0){
        vector<string> msgs;
        split(msgs,buf,(const char*)":");
        packet.clear();//return zero str
    }
    bzero(buf,l);//erasing previous data
    return packet;//if it is a chat message return it else return empty str
}