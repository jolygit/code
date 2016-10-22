/* include fig01 */
#include	"unp.h"
#include	<limits.h>		/* for OPEN_MAX */
#include        "database.h"
#include        <boost/algorithm/string.hpp>
#include        <vector>
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
int RegisterOrLogin(int sockfd,bool& clLogin,char const* buf,char* clAddress);
int
main(int argc, char **argv)
{
	int					i, maxi, listenfd, connfd, sockfd;
	int					nready;
	ssize_t				n;
	char				buf[MAXLINE];
	socklen_t			clilen;
	int myOPEN_MAX = sysconf (_SC_OPEN_MAX);
	printf("%d\n",myOPEN_MAX);
	struct pollfd		client[myOPEN_MAX];
	struct sockaddr_in	cliaddr, servaddr;
        bool                    clLogin[myOPEN_MAX];
	listenfd = Socket(AF_INET, SOCK_STREAM, 0);

	bzero(&servaddr, sizeof(servaddr));
	servaddr.sin_family      = AF_INET;
	servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
	servaddr.sin_port        = htons(SERV_PORT);
       

	 Bind(listenfd, (SA *) &servaddr, sizeof(servaddr));

 	Listen(listenfd, LISTENQ);

 	client[0].fd = listenfd;
 	client[0].events = POLLRDNORM;
	clLogin[0]=false;
 	for (i = 1; i < myOPEN_MAX; i++){
 		client[i].fd = -1;		/* -1 indicates available entry */
		clLogin[i]=false;
	}
		maxi = 0;					/* max index into client[] array */
 /* end fig01 */

 /* include fig02 */
		for ( ; ; ) {
		  nready = Poll(client, maxi+1, INFTIM);

		  if (client[0].revents & POLLRDNORM) {	/* new client connection */
		    clilen = sizeof(cliaddr);
		    connfd = Accept(listenfd, (SA *) &cliaddr, &clilen);
		    //#ifdef	NOTDEF
		    printf("new client: %s\n", Sock_ntop((SA *) &cliaddr, clilen));
		    //#endif

		    for (i = 1; i < myOPEN_MAX; i++)
		      if (client[i].fd < 0) {
			client[i].fd = connfd;	/* save descriptor */
			break;
		      }
		    if (i == myOPEN_MAX)
		      err_quit("too many clients");

		    client[i].events = POLLRDNORM;
		    if (i > maxi)
		      maxi = i;				/* max index in client[] array */

		    if (--nready <= 0)
		      continue;				/* no more readable descriptors */
		  }

		  for (i = 1; i <= maxi; i++) {	/* check all clients for data */
		    if ( (sockfd = client[i].fd) < 0)
		      continue;
		    if (client[i].revents & (POLLRDNORM | POLLERR)) {
		      if ( (n = read(sockfd, buf, MAXLINE)) < 0) {
			if (errno == ECONNRESET) {
			  /*4connection reset by client */
#ifdef	NOTDEF
			  printf("client[%d] aborted connection\n", i);
#endif
			  Close(sockfd);
			  client[i].fd = -1;
			  clLogin[i]=false;
			} else
			  err_sys("read error");
		      } else if (n == 0) {
			/*4connection closed by client */
#ifdef	NOTDEF
			printf("client[%d] closed connection\n", i);
#endif
			Close(sockfd);
			client[i].fd = -1;
			clLogin[i]=false;
		      }
		      else if(clLogin[i]==false){
			//	printf("%s",buf);
			char* clAddress=Sock_ntop((SA *) &cliaddr, clilen);
			if(RegisterOrLogin(sockfd,clLogin[i],buf,clAddress)==0)
			  clLogin[i]=true;
		      }
		      else{
			//	Writen(sockfd, buf, n);
		      }

		      if (--nready <= 0)
			break;				/* no more readable descriptors */
		    }
		  }
		}
}
/* end fig02 */
int Register(vector<string> &strs,string & msg,char* clAddress){
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
  DataBase db;   
  db.ConnectToCollection("users","users");
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
int Login(vector<string> &strs,string & msg,char* clAddress){
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
  DataBase db;   
  db.ConnectToCollection("users","users");
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
int RegisterOrLogin(int sockfd,bool& clLogin,char const* buf,char* clAddress){
  string  str(buf);
  vector<string> strs;
  if(str.compare(0,11,"Register::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    // cout << "* size of the vector: " << strs.size() << endl;
    // if(strs.size()!=6)
    //   printf("wrong number of fields\n");
    //  for (size_t i = 0; i < strs.size(); i++)
    //    cout << strs[i] << endl;
    string msg("ok");
    if(Register(strs,msg,clAddress)==0)
      printf("registration ok\n");
    Writen(sockfd,(void*)msg.c_str(),msg.length()+1);
    return 0;
  }
  else if(str.compare(0,8,"Login::,")==0){//Register:
    boost::split(strs,str,boost::is_any_of(","));
    if(strs.size()!=3)
      printf("wrong number of fields\n");
  
    // cout << "* size of the vector: " << strs.size() << endl;    
    // for (size_t i = 1; i < strs.size(); i++)
    //   cout << strs[i] << endl;
    string msg("ok");
    if(Login(strs,msg,clAddress)==0)
      printf("login ok\n");
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
