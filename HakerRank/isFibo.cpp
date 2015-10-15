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
int intlog(double base, double x) {
    return (int)(log(x) / log(base));
}
class matrix{
public:
  matrix();
  void mProduct(matrix& B);
  void clear();
  long long M[2][2];
};
matrix M,B;
void matrix::clear(){
  M[0][0]=1;
  M[0][1]=1;
  M[1][0]=1;
  M[1][1]=0;
}
matrix::matrix(){
  M[0][0]=1;
  M[0][1]=1;
  M[1][0]=1;
  M[1][1]=0;
}

void  matrix::mProduct(matrix& B){
  long long C[2][2];
  for(int i=0;i<2;i++){
    for(int j=0;j<2;j++){
      C[i][j]=0;
      for(int k=0;k<2;k++)
	C[i][j]=C[i][j]+M[i][k]*B.M[k][j];
    }
  }
  for(int i=0;i<2;i++)
    for(int j=0;j<2;j++)
      M[i][j]=C[i][j];
}


void matpow(int n)
{
  if (n > 1) {
    matpow(n/2);
    M.mProduct(M);
    if (n%2==1)
      M.mProduct(B);
  }
}
long long fib(int n)
{
  M.clear();
  matpow(n-1);
  return M.M[0][0];
}
int main() {
  int numT=1;
  cin >> numT;
  vector<bool> yesOrNo;
  double fay=(1+sqrt(5))/2;
  for(int i=0;i<numT;i++){
    long long num=10000000000;
    cin >> num;
    int n=1;
    if(num>1000)
      n=intlog(fay,num*sqrt(5))-1;
    long long value;
    while((value=fib(n))<num)
      n++;
    if(value==num)
      yesOrNo.push_back(true);
    else
      yesOrNo.push_back(false);
  }
  for(int i=0;i<numT;i++)
    yesOrNo[i]?printf("IsFibo\n"):printf("IsNotFibo\n");
 return 0;
}
