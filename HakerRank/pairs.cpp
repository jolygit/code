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
  long long n=5,k=2,N;
  cin >>n>>k;
  N=n;
  vector<long long> vals;
  set<long long > nums;
  // int arr[5]={1,5,3,4,2};
  while(n--){
    long long n1;
    cin >>n1;
    // n1=arr[4-n];
    vals.push_back(n1);
    nums.insert(n1);
  }
  sort(vals.begin(),vals.end());
  int nump=0;
  while(N--){
    long long v=vals[vals.size()-N-1]+k;
    if(nums.find(v)!=nums.end())
      nump++;
  }
  cout<<nump<<endl;
   return 0;
}
