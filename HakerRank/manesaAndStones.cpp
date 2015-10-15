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

int main() {
  int N=1;
  cin >> N;
  vector<set<int> > values;
  for(int j=0;j<N;j++){
    long n=3;
    cin >> n;
    long a=1;
    cin >> a;
    long b=2;
    cin >> b;
    set<int>  tmpvalues;
    for(int i=0;i<n;i++){
      int tot=i*a+(n-i-1)*b;
      tmpvalues.insert(tot);
    }
    values.push_back(tmpvalues);
  }
  for(int i=0; i<values.size();i++){
    set<int>&  tmpvalues=values[i];
    set<int>::iterator it;
    for (it = tmpvalues.begin(); it != tmpvalues.end(); ++it)
      	it==tmpvalues.begin()?printf("%d",*it):printf(" %d",*it);
    printf("\n");
  }
  return 0;
}
