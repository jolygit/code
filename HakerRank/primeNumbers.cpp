#include <map>
#include <set>
#include <list>
#include <cmath>
#include <ctime>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <cstdio>
#include <vector>
#include <cstdlib>
#include <numeric>
#include <sstream>
#include <iostream>
#include <algorithm>
using namespace std;
bool primeOrNot(int num){
  if(num<=3){
    if(num<=1)
      return false;
    else
      return true;
  }
  if(num%2==0 || num%3==0)
    return false;
  for (int i=5;i<int(sqrt(num))+1;i+=6)
    if(num%i==0 ||num%(i+2)==0)
      return false;
  return true;
}
int getNumberOfPrimes(int N) {
  int numP=0;
  for(int i=2;i<N;i++)
    if(primeOrNot(i))
      numP++;
  return numP;
}
int main() {
	int res;
	int _a=1000000;
	//cin >> _a;
	res = getNumberOfPrimes(_a);
	printf("%d", res);
	return 0;
}
