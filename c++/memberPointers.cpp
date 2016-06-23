#include <iostream>
using namespace std;

struct Test {
  int num;
  void func() {}
};

// Notice the extra "Test::" in the pointer type
int Test::*ptr_num = &Test::num;
void (Test::*ptr_func)() = &Test::func;

int main() {
  Test t;
  Test *pt = new Test;

  // Call the stored member function
  (t.*ptr_func)();
  (pt->*ptr_func)();

  // Set the variable in the stored member slot
  t.*ptr_num = 1;
  pt->*ptr_num = 2;

  delete pt;
  return 0;
}
