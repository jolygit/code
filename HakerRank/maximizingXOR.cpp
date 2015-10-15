#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT */   
   int t=1,n=0;
   cin>>t;
   cin>>n;
   vector<int> h;
   while(t--){
    int  hi=1;
     for(int i=0;i<=n;i++){
       if((i%2)==0)
	 hi++;
       else
	 hi*=2;
       h.push_back(hi);
     }
   }
   for(int i=0;i<h.size();i++)
    cout<<h[i]<<endl;
    return 0;
}
