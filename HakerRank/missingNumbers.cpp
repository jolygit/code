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
  long long n1=1,n;
  cin >>n1;
  vector<long long> a;
  map<int, int> aCnt;
  map<int,int>::iterator ita;
  while(n1--){
    cin >>n;
    a.push_back(n);
    ita=aCnt.find(n);
    if(ita==aCnt.end())
      aCnt[n]=1;
    else
      aCnt[n]=aCnt[n]+1;
  }
  long long n2=1;
  cin >>n2;
  vector<long long> b;
  map<int, int> bCnt;
  map<int, int> alr;
  while(n2--){
    cin >>n;
    b.push_back(n);
    ita=bCnt.find(n);
    if(ita==bCnt.end())
      bCnt[n]=1;
    else
      bCnt[n]=bCnt[n]+1;
  }
  vector<long long> m;
  for(int i=0;i<b.size();i++)
    if(alr.find(b[i])==alr.end() && (aCnt.find(b[i])==aCnt.end() || bCnt[b[i]]!=aCnt[b[i]])){
      m.push_back(b[i]);
      alr[b[i]]=1;
    }
  sort(m.begin(),m.end());
  for(int i=0;i<m.size();i++)
    cout<<m[i]<<" ";
  cout<<endl;
   return 0;
}
