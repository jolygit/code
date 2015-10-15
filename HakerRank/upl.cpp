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
  int num=0;
  string input;
  string token;
  getline(cin,input);
  stringstream stream(input);
  while (getline(stream,token,' '))
    num+=atoi(token.c_str());
  printf("%d\n",num);
  return 0;
}
