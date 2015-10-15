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

int main() {
  int N=1;
  cin >> N;
  int M=1;
  cin >> M;
  long long total=0;
  for(int i=0;i<M;i++){
    long num1=0;
    cin >> num1;
    long num2=0;
    cin >> num2;
    long num3=0;
    cin >> num3;
    total+=(num2-num1+1)*num3;
  }
  long long v=total/N;
  v*N>total?printf("%lld\n",(total/N)-1):printf("%lld\n",(total/N));
  return 0;
}
