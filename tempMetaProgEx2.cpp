/*

    Copyright David Abrahams 2003-2004
    Copyright Aleksey Gurtovoy 2003-2004

    Distributed under the Boost Software License, Version 1.0. 
    (See accompanying file LICENSE_1_0.txt or copy at 
    http://www.boost.org/LICENSE_1_0.txt)
            
    This file was automatically extracted from the source of 
    "C++ Template Metaprogramming", by David Abrahams and 
    Aleksey Gurtovoy.

    It was built successfully with GCC 3.4.2 on Windows using
    the following command: 

        g++ -I..\..\boost_1_32_0  -o%TEMP%\metaprogram-chapter1-example1.exe example1.cpp
        

*/

#include "libs/mpl/book/chapter1/binary.hpp"
#include "boost/type_traits/is_same.hpp"
#include "boost/type_traits/is_convertible.hpp"
#include <iostream>
template <class T,bool val> struct choose;
template<class T> struct choose<T,true>{
  typedef T Type_value;
};
template<class T> struct choose<T,false>{
typedef T const& Type_value;
};

template<class T> class add_const_ref{
static bool const val=boost::is_reference<T>::value;
typedef typename choose<T,val>::Type_value Type_value;
};
using namespace std;
int main()
{
bool test=boost::is_same<add_const_ref<int>::Type_value,int&>::value;
cout<<test<<endl;
    return 0;
}
