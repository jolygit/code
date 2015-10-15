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

int main(void) {
  int t;
  long long n,k;
  cin >>t;
  vector<string> res;
  while(t--){
    long long n,k;
    cin >>n>>k;
    vector <long long>  a1;
    vector <long long>  a2;
    long long n1=n,v;
    while(n1--){
      cin >> v;
      a1.push_back(v); 
    }
    int n2=n;
    while(n2--){
      cin >> v;
      a2.push_back(v); 
    }
    sort(a1.begin(),a1.end());
    sort(a2.begin(),a2.end());
    int cnt=0;
    for(int i=0;i<a1.size();i++){
      for(int j=0;j<a1.size();j++){
	if(a1[i]+a2[j]>=k && a2[j]!=0){
	  a2[j]=0;
	  cnt++;
	  break;
	}
      }
    }
    if(cnt==n)
      res.push_back("YES");
    else
      res.push_back("NO");
  }
 for(int i=0;i<res.size();i++)
   cout<<res[i]<<endl;
   return 0;
}
