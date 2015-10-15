#include <stdio.h>
#include <typeinfo>


class B{
  int k;
public:
  int g;
};

class A:protected B{
public:
  void print(){
    int pr=g;
  }
};

class D:public A{
  void f(){
    int gg=g;
  }
};
int main()
{
  A a;
int d=a.g;

  return 0;
}

