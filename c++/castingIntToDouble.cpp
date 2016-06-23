
template<int N> struct fac{
  static long long const val=N*fac<N-1>::val;
};
template<> struct fac<0>{
  static long long const val=1;
};

int  main()
{
  int f=fac<20>::val;
  //f(4,3); 
    return 0;
}
