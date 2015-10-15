#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
#include <sstream>
#include <iostream>
#include <fstream>
using namespace std;

int main() {
  int num,numVal=-1;
  int plast=0,last=0;
  int cnt=0,missing;
  string input;
  string token;
  ifstream myfile( "example.csv" );
  getline(cin,input);//myfile
  numVal=atoi(input.c_str());
  getline(cin,input);//myfile
  stringstream stream(input);
  while (getline(stream,token,' ')){
    num=atoi(token.c_str());
    if (cnt==numVal)
      break;
    if(cnt==1 || cnt==0){
      plast=last;
      last=num;
    }
    else{
      int dist1=last-plast;
      int dist2=num-last;
      if(dist1!=dist2 && dist1>0){
	missing=dist1>dist2?plast+dist2:last+dist1;
      }
      if(dist1!=dist2 && dist1<0){
	missing=dist1>dist2?last+dist1:plast+dist2;
      }
      plast=last;
      last=num;
    }
    cnt++;
  }
  printf("%d\n",missing);
  return 0;
}
