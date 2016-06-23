#include <iostream>
#include <string>
#include <stdlib.h>
#include <cfloat>
using namespace std;
class base2{
public:
  virtual int pr2()=0;
virtual  ~base2(){
    cout << ":destructor:base2"<<endl;
  }
 
};

class Wilma{
  int v;
public:
  virtual void prb()=0;
  void pr(){
    prb();// cout <<"pringing"<<endl;
  }

};

class Fred:public Wilma{
public:
  virtual void prb(){
    cout <<"overide print"<<endl;
  }
  void print(){
    pr();// cout <<"pringing"<<endl;
  }
};
struct fld{
  bool f:1;
  bool f2:1;
  bool f3:1;
  bool f4:1;
};
int main(){
  
  int v=3;
  fld* pv=new fld;
  
  for(int k=0;k<20;k++){
  double r=rand(),m=RAND_MAX;
  double rr=r/m;
  printf("%g\n",rr);
  }
  fld ff;
  ff.f=1;
  ff.f2=ff.f;
  double p=1234567899999;
  long long i=sizeof(fld);
  char st[]="vova";
  enum Dir {first,second,third};
#ifdef __cplusplus
  printf("%d\n",DBL_DIG);
#endif
  Fred f;
  f.print();
  return 1;
}
