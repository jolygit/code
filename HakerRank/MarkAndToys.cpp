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
  vector <long long>  _ar;
  long long n,k;
  cin >> n>>k;
  while(n--){
    int _ar_tmp;
    cin >> _ar_tmp;
    _ar.push_back(_ar_tmp); 
  }
  sort(_ar.begin(),_ar.end());
  long long tot=0,cnt=0;
  for(int i=0;i<_ar.size();i++){
    //  cout<<k<<" "<<tot+_ar[i]<<" "<<cnt;

    if((tot+_ar[i])<=k){
      tot+=_ar[i];
      cnt++;
    }
    else
      break;
  }
  cout<<cnt<<endl; 
  return 0;
}
