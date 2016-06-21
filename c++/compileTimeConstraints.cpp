template<class T, class B> struct Derived_from {
  static void constraints(T* p) { B* pb = p; }
  Derived_from() {}// void(*p)(T*) = constraints; }
};

// template<class T1, class T2> struct Can_copy {
//   static void constraints(T1 a, T2 b) { T2 c = a; b = a; }
//   Can_copy() { void(*p)(T1,T2) = constraints; }
// };

// template<class T1, class T2 = T1> struct Can_compare {
//   static void constraints(T1 a, T2 b) { a==b; a!=b; a<b; }
//   Can_compare() { void(*p)(T1,T2) = constraints; }
// };

// template<class T1, class T2, class T3 = T1> struct Can_multiply {
//   static void constraints(T1 a, T2 b, T3 c) { c = a*b; }
//   Can_multiply() { void(*p)(T1,T2,T3) = constraints; }
// };

struct B { };
struct D : B { };
struct DD : D { };
struct X { };

int main()
{
  Derived_from<D,B>();
  Derived_from<DD,B>();
  Derived_from<X,B>();
  Derived_from<int,B>();
  Derived_from<X,int>();

  // Can_compare<int,float>();
  // Can_compare<X,B>();
  // Can_multiply<int,float>();
  // Can_multiply<int,float,double>();
  // Can_multiply<B,X>();
	
  // Can_copy<D*,B*>();
  // Can_copy<D,B*>();
  // Can_copy<int,B*>();
}
