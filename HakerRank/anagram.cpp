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

int main(void) {
  long long t=1;
   cin >>t;
  vector<int> res;
  while(t--){
    string input;
    map<char,int> s1;
    map<char,int> s2;
    cin>>input;
    if(input.size()%2==1){
      res.push_back(-1);
      continue;
    }
    for(int i=0;i<input.size()/2;i++){
      if(s1.find(input[i])==s1.end())
	s1[input[i]]=1;
      else
	s1[input[i]]++;
      if(s2.find(input[i+input.size()/2])==s2.end())
	s2[input[i+input.size()/2]]=1;
      else
	s2[input[i+input.size()/2]]++;
    }
    int cnt=0;
    for(map<char,int>::iterator it=s1.begin();it!=s1.end();it++){
      char key1=it->first;
      int val1=it->second;
      if(s2.find(key1)==s2.end())
	cnt+=val1;
      else if(val1-s2[key1]>0)
	cnt+=abs(val1-s2[key1]);
    }
    res.push_back(cnt);
  }
  for(int i=0;i<res.size();i++){
    cout<<res[i]<<endl;
  }
   return 0;
}
