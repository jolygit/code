

#include "libs/mpl/book/chapter1/binary.hpp"
#include "boost/type_traits/is_same.hpp"
#include "boost/type_traits/is_convertible.hpp"
#include <iostream>

template <class C, class X, class Y>
struct replace_type;
template <class C, class X, class Y, bool same>
struct replace_type_impl;
template <class C, class X, class Y>
struct replace_type_impl<C,X,Y,true>
{
typedef Y type;
};
template <class C, class X, class Y>
struct replace_type_impl<C,X,Y,false>
{
typedef C type;
};
template <class C, class X, class Y>
struct replace_type_impl<C*,X,Y,false>
{
typedef typename replace_type<C,X,Y>::type * type;
};
template <class C, class X, class Y>
struct replace_type_impl<C const,X,Y,false>
{
typedef typename replace_type<C,X,Y>::type const type;
};
template <class C, class X, class Y>
struct replace_type_impl<C &,X,Y,false>
{
typedef typename replace_type<C,X,Y>::type & type;
};
template <class C, class X, class Y>
struct replace_type_impl<C[],X,Y,false>
{
typedef typename replace_type<C,X,Y>::type type [];
};
template <class C, class X, class Y, int N>
struct replace_type_impl<C[N],X,Y,false>
{
typedef typename replace_type<C,X,Y>::type type [N];
};
template <typename R, typename X, typename Y>
struct replace_type_impl<R (), X, Y, false>
{
typedef typename replace_type<R, X, Y>::type return_type;
typedef return_type type();
};
template <class X, class Y, class A, class B>
struct replace_type_impl<A(*)(B),X,Y,false>
{
typedef typename replace_type<A,X,Y>::type (*type)(typename replace_type<B,X,Y>::type);
};
template <class X, class Y, class A, class B, class C>
struct replace_type_impl<A(*)(B,C),X,Y,false>
{
typedef typename replace_type<A,X,Y>::type (*type)(typename replace_type<B,X,Y>::type, typename replace_type<C,X,Y>::type);
};
template <class C, class X, class Y>
struct replace_type
{
static const bool is_same = boost::is_same<C, X>::value;
typedef typename replace_type_impl<C,X,Y,is_same>::type type;
};
using namespace std;
int main()
{
bool test1=boost::is_same<replace_type<int* (*)(char),int,long>::type,long* (*)(int)>::value;

cout<<test1<<endl;
    return 0;
}

