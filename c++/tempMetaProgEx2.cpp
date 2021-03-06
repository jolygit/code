#include "libs/mpl/book/chapter1/binary.hpp"
#include "boost/type_traits/is_same.hpp"
#include "boost/type_traits/is_convertible.hpp"
#include "boost/type_traits/is_reference.hpp"
#include "boost/type_traits/is_pointer.hpp"
#include <iostream>
#include <cassert>
#include <boost/type_traits/remove_reference.hpp>
#include <boost/type_traits.hpp>
#include <boost/mpl/vector.hpp>
using namespace std;

template <class target>
struct bounds{
static const size_t value=0;
};

template <class target,size_t N>
struct bounds<target[N]>{
static const size_t value=N;
};
template <class target>
class type_descriptor
{

public:
std::ostream&    print(std::ostream & out) const{
if(boost::is_volatile<target>::value){
typedef typename boost::remove_volatile<target>::type newtype;
out<<type_descriptor<newtype>()<<" volatile";
}
 else if(boost::is_const<target>::value){
typedef typename boost::remove_const<target>::type newtype;
out<<type_descriptor<newtype>()<<" const";
}
 else if(boost::is_reference<target>::value){
typedef typename boost::remove_reference<target>::type newtype;
out<<type_descriptor<newtype>()<<"&";
}
 else if(boost::is_pointer<target>::value){
typedef typename boost::remove_pointer<target>::type newtype;
out<<type_descriptor<newtype>()<<"*";
}
 else if(boost::is_array<target>::value){
typedef typename boost::remove_bounds<target>::type newtype;
out<<type_descriptor<newtype>()<<"["<<bounds<target>::value<<"]";
}
 else if(boost::is_same<target,int>::value)
   out<<"int";
 else if(boost::is_same<target,long int>::value){
out<<"long int";
}
 else if(boost::is_same<target,char>::value){
out<<"char";
}
 else if(boost::is_same<target,short int>::value){
out<<"short int";
}
 else if(boost::is_same<target,short int>::value){
out<<"short int";
}
 else
   out<<"unknown type";
return out;

}
};
template<typename T>
std::ostream& operator<<(std::ostream& os, const  type_descriptor<T>& obj)
{
return obj.print(os);
}

template <size_t N>
struct int_{
static const size_t value=N;
};

class A{
public:
  A(){
    cout<<"A const called"<<endl;
  }
};
class B:public A{
public:
  B(){
    cout<<"B const called"<<endl;
  }
};

int main()
{
  B bb;
  int a=3;
  int b=4;
  int& r=a;
  r=b;
  static const int vv=int_<3>::value;
  typedef boost::mpl::vector<int_<2>,int_<3>> mytype;
  typedef boost::mpl::vector<int_<3>,int_<2>> mytype1;
bool val=  boost::is_same<mytype,mytype>::value;
//  cout<<  type_descriptor<int>() <<endl;
cout<<type_descriptor<int const*& >()<<endl;
//cout<< type;
return 0;
}

