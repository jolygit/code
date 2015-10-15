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
#include <utility>      // std::pair
using namespace std;
long long* maxLst;
void PreComputeMax(long long* vals,long long size){
  long long max=0;
  for (long long i=0;i<size;i++) {
    long long val=*(vals+size-1-i);
    max=val>max?val:max;
    *(maxLst+size-1-i)=max;
  }
}
long long max(long long* vals,long long* maxLst,long long size){
  long long maxp=0;
  if(size<=1)
    return maxp;
  else{
    long long fel=*vals;
    long long maxv=*maxLst;
    maxp=max(vals+1,maxLst+1,size-1);
    if(fel<maxv)
      maxp+=maxv-fel;
  }
  return maxp;
}
int main(void) {
  long long n=1,t=1;
  cin >>t;
  vector<long long> res;
  while(t--){
    long long n=3,size;
    long long* vals;
    cin >>n;
    size=n;
    vals=(long long*)malloc(sizeof(long long)*size);
    maxLst=(long long*)malloc(sizeof(long long)*size);
    for(long long i=0;i<n;i++){
      long long n1;
      cin >>n1;
      *(vals+i)=n1;
    }
    PreComputeMax(vals,size);
    res.push_back(max(vals,maxLst,size));
    free(vals);
    free(maxLst);
  }
  for(long long i=0;i<res.size();i++)
    cout<<res[i]<<endl;
   return 0;
}
