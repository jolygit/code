

#include "libs/mpl/book/chapter1/binary.hpp"
#include "boost/type_traits/is_same.hpp"
#include "boost/type_traits/is_convertible.hpp"
#include "boost/type_traits/is_reference.hpp"
#include "boost/type_traits/is_pointer.hpp"
#include <iostream>
#include <cassert>
#include <boost/type_traits/remove_reference.hpp>

template <class target,class source>
inline target polymorphic_downcast(source* x)
{
  assert(dynamic_cast<target>(x)==x);
  return static_cast<target>(x);
};
template <class target,class source>
inline target polymorphic_downcast1(source& x)
{
  typedef typename boost::remove_reference<target>::type noref;
  assert(dynamic_cast<noref*>(&x) == &x);
  return static_cast<target>(x);
};
struct A{
  virtual  ~A(){};};
struct B: A{};
using namespace std;
int main()
{
  B b; 
  A* pa=&b;
  B* pb=polymorphic_downcast<B*>(pa);
  A& ra=b;
  B& rb=polymorphic_downcast1<B&>(ra);
  return 0;
}

