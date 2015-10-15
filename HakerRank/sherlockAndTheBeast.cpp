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
string decent(long num){
 string str1("-1");
  if(num<3)
    return str1;
  int num5=0,num3=0;
  long div=num/3;
  for(int i=div;i>=0;i--){
    long res=num-i*3;
    if(res%5==0){
      num5=i*3;
      num3=num-num5;
      break;
    }
  }
  string str;
  for (int i=0;i<num5;i++)
    str.append("5");
  for (int i=0;i<num3;i++)
    str.append("3");
  if(num5==0 && num3==0)
    return str1;
  else
    return str;
}
int main() {
  int N=1;
  cin >> N;
  vector<string> nums;
  for(int i=0;i<N;i++){
    long num=7;
    cin >> num;
    string value=decent(num);
    nums.push_back(value);
  }
  for(int i=0;i<N;i++)
    printf("%s\n",nums[i].c_str());
  return 0;
}
