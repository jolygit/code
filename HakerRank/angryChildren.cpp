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
  int N=0;
  cin >> N;
  int K=0;
  cin >> K;
  vector<long> packetSizes;
  for(int i=0;i<N;i++){
    long num=0;
    cin >> num;
    packetSizes.push_back(num);
  }
  sort(packetSizes.begin(),packetSizes.end());
  long dist=1000000000;
  for(int i=0;i<N-K;i++){
    if(packetSizes[i+K-1]-packetSizes[i]<dist)
      dist=packetSizes[i+K-1]-packetSizes[i];
  }
  dist=N==K?(packetSizes[N-1]-packetSizes[0]):dist;
  printf("%ld\n",dist);
 return 0;
}
