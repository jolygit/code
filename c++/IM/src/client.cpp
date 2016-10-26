#include "clientProcessor.h"

int
main(int argc, char **argv)
{
  int opt,r,l;
  bool uregister=false,login=false;
  char *address=NULL;
  while ((opt = getopt(argc, argv, ":a:rlx")) != -1) {
    switch (opt) {
    case 'a': address = optarg;     break;
    case 'r': uregister=true;       break;
    case 'l': login=true;           break;
      // case ':': usageError(argv[0], "Missing argument", optopt);
      //  case '?': usageError(argv[0], "Unrecognized option", optopt);
    default:  abort();//fatal("Unexpected case in switch()");
    }
  }
  if (address == NULL)   
    err_quit("-a was not specified with the address");
  int			sockfd;
  struct sockaddr_in	servaddr;
  ClientProcessor clProcessor;
  sockfd = Socket(AF_INET, SOCK_STREAM, 0);
  bzero(&servaddr, sizeof(servaddr));
  servaddr.sin_family = AF_INET;
  servaddr.sin_port = htons(SERV_PORT);
  Inet_pton(AF_INET, address, &servaddr.sin_addr);
  Connect(sockfd, (SA *) &servaddr, sizeof(servaddr));
  if(uregister)
    clProcessor.Register(sockfd,true);
  else if(login)
    clProcessor.Register(sockfd,false);
  else{
    printf("you have to either register or login use -r or -l options respectively\n");
  }
  clProcessor.Commands(sockfd);
  //	clProcessor.ClientServer(sockfd);
}
