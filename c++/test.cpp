// #include <stdio.h>
// #include <iostream>
// #include <typeinfo>
// #include "my.h"
// #include <vector>
// class Base
// {
// public:
//     void* operator new(size_t sz)
//     {
//       std::cerr << "new " << sz << " bytes\n";
//         return ::operator new(sz);
//     }

//     void operator delete(void* p)
//     {
//         std::cerr << "delete\n";
//         ::operator delete(p);
//     }
// private:
//     int m_data;
// };


// class Derived : public Base
// {
// private:
//     int m_derived_data;
//   std::vector<int> z, y, x, w;
// };

// class Trace {
//  public:
//   Trace( const char *msg )
//     : m_( msg ) { std::cout << "Entering " << m_ << std::endl; }
//   ~Trace(){
//     std::cout << "Exiting " << m_ << std::endl;
//     delete this;
//   }
//  private:
//   const char *m_;
// };
// int main()
// {
//     Base* b = new Base;
//     delete b;
//     for (int i=0;i<3;i++){
//       Trace* tr=new Trace("loop");
//     }
//     Derived* d = new Derived;
//     delete d;
//     return 0;
// }

 #include <stdio.h>

int f(int k,int g){
  k++;
  return k+g;
}
   int main()
   {
      
     
     char i=-2;
     i=i+3;
     for(i=0;i<3;i++)
       int g=1;
     return 0;
   }
