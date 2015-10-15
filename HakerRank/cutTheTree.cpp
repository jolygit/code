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
map<int,int> done;
int Sum(int b,map<int,vector<int> >& neib,vector<int>& vals){
  int sum=0;
  sum+=vals[b-1];
  vector<int>& nb=neib[b];
  for(int i=0;i<nb.size();i++){
    if(nb[i]!=-1 ){
      vector<int>& nb1=neib[nb[i]];
      for(int j=0;j<nb1.size();j++)
	if(nb1[j]==b)
	  nb1[j]=-1;
      sum+=Sum(nb[i],neib,vals);
    }
  }
  return sum;
}
int main(void) {
  long long n1=6,n,N;
  cin >>n1;
  N=n1-1;
  vector<int> vals;
  vector<pair<int,int> > ed;
  map<int,vector<int> > neib;
  int Tsum=0,dist;
  // int arr[6]={100,200,100,500,100,600};
  // vector<pair<int,int> > ed1;
  // ed1.push_back(make_pair(1,2));
  // ed1.push_back(make_pair(2,3));
  // ed1.push_back(make_pair(2,5));
  // ed1.push_back(make_pair(4,5));
  // ed1.push_back(make_pair(5,6));
  while(n1--){
     cin >>n;
     //n=arr[5-n1];
    vals.push_back(n);
    Tsum+=n;
  }
  dist=Tsum;
  while(N--){
    int k;
    cin >>n>>k;
    //n=ed1[ed1.size()-1-N].first;
    //k=ed1[ed1.size()-1-N].second;
    ed.push_back(make_pair(n,k));
    if(neib.find(k)==neib.end()){
      vector<int> e;
      e.push_back(n);
      neib[k]=e;
    }
    else
      neib[k].push_back(n);
    if(neib.find(n)==neib.end()){
      vector<int> e;
      e.push_back(k);
      neib[n]=e;
    }
    else
      neib[n].push_back(k);
  }
  for(int i=0;i<ed.size();i++){
    map<int,vector<int> > neib1=neib;
    pair<int,int>  br=ed[i];
    int a=br.first;
    int b=br.second;
    vector<int>& e=neib1[a];
    for(int d=0;d<e.size();d++){
      if(e[d]==b)
	e[d]=-1;
    }
    vector<int>& ea=neib1[b];
    for(int d=0;d<ea.size();d++){
      if(ea[d]==a)
	ea[d]=-1;
    }
    int sum=Sum(b,neib1,vals);
    if(abs(Tsum-2*sum)<dist)
      dist=abs(Tsum-2*sum);
  }

  cout<<dist<<endl;
   return 0;
}
