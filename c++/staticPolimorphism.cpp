#include <iostream>
#include <vector>
template <class T> 
struct Base
{
    void interface()
    {
        // ...
        static_cast<T*>(this)->implementation();
        // ...
    }

    // static void static_func()
    // {
    //     // ...
    //     T::static_sub_func();
    //     // ...
    // }
  void implementation(){
    printf("I am base\n" );
  }
};

struct Derived : Base<Derived>
{
  void implementation(){
    printf("I am derived\n" );
  }
    static void static_sub_func();
};

struct Derived1 : Base<Derived1>
{
  void implementation(){
    printf("I am derived1\n" );
  }
    static void static_sub_func();
};

int main()
{
  // Derived1 d1;
  // Derived d;
  // //  std::Vector
  // d1.interface();
  // d.interface();
  //  printf("adsfs\n" );

  }
