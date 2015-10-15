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
  long long t=1;
  cin >>t;
  long long n=4,s=0,cnt=0;
  vector<string> answer(t,"NO");
  while(t--){
    cin >>n;
    s=0;
    // int arr[4]={1,2,3,3};
    vector<long long> dq;
    while(n--){
      long long k;//arr[3-n];
      cin >>k;
      dq.push_back(k);
      s+=k;
    }
    long long s1=0,l=0,r=0;
    for(int i=0;i<dq.size();i++){
      s1+=dq[i];
      l=s1-dq[i];
      r=s-s1;
      if(l==r){
	answer[cnt]="YES";
	break;
      }
    }
    cnt++;
  }
  for(int i=0;i<answer.size();i++){
    cout<<answer[i]<<endl;
  }
   
   return 0;
}
