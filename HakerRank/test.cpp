
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <sstream>
#include <iostream>

using namespace std;

int main() {
  int num,numVal=-1;
  int plast=0,last=0;
  int cnt=0,missing;
  string input;
  string token;
  getline(cin,input);
  numVal=atoi(input.c_str());
  getline(cin,input);
  stringstream stream(input);
  while (getline(stream,token,' ')){
    num=atoi(token.c_str());
    if (cnt==numVal)
      break;
    if(cnt==1 || cnt==2){
      plast=last;
      last=num;
    }
    else{
      int dist1=last-plast;
      int dist2=num-last;
      if(dist1!=dist2){
	missing=dist1>dist2?plast+dist1/2:last+dist1;
      }
      plast=last;
      last=num;
    }
    cnt++;
  }
  printf("%d\n",missing);
  return 0;
}
