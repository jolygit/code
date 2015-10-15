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
void split(vector<int>& ar,vector<int>& l,vector<int>& r) {
int p=ar[0];
 for(int i=1; i<ar.size(); i++) 
   ar[i]>p?r.push_back(ar[i]):l.push_back(ar[i]); 
}
void quickSort(vector<int>&  ar) {
  vector<int>  l;
  vector<int>  r;
  int p=ar[0];
  split(ar,l,r);
  // cout<<l.size()<<" "<<r.size()<<endl;
  if(l.size()>1)
    quickSort(l);
  if(r.size()>1)
    quickSort(r);
  for(int i=0; i<l.size(); i++) {
    cout<<l[i]<<" ";
    ar[i]=l[i];
  }
  ar[l.size()]=p;
  cout<<p<<" ";
  for(int i=0; i<r.size(); i++) {
    cout<<r[i]<<" ";
    ar[l.size()+i+1]=r[i];
  }
cout<<endl;
}
int main(void) {
  vector <int>  _ar;
  int _ar_size;
  cin >> _ar_size;
  for(int _ar_i=0; _ar_i<_ar_size; _ar_i++) {
    int _ar_tmp;
    cin >> _ar_tmp;
    _ar.push_back(_ar_tmp); 
  }

  quickSort(_ar);
   
  return 0;
}
