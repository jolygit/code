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
#include <deque>
using namespace std;

int main(void) {
  long long n=4;
  cin >>n;
  long N=n;
  long long s=0,cnt=1,p0=0;
  vector<long long> dq;
  while(n--){
    cin >>k;
    dq.push_back(k);
    s+=k;
    p0+=cnt*k;
    cnt++;
  }
  long long pmean=p0,maxpmean=p0;
  for(int i=0;i<N;i++){
    long long k=dq[N-i-1];
    pmean=pmean-k*N+s;
    if(pmean>maxpmean)
      maxpmean=pmean;
  }
   cout<<maxpmean<<endl;
   return 0;
}
