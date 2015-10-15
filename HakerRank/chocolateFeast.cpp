#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
    /* Enter your code here. Read input from STDIN. Print output to STDOUT */   
    int t=1,n=6,c=2,m=2;
    cin>>t;
    vector<int> answrs;
    while(t--){
      cin>>n>>c>>m;
        int answer=0;
	int numC=n/c;
	answer=numC;
	int additional=numC;
	while(additional>0){
	  int left=additional%m;
	  additional=additional/m;
	  if(!additional)
	    break;
	  answer=answer+additional;
	  additional+=left;
	}
        // Computer answer
        answrs.push_back(answer);
    }
    for(int i=0;i<answrs.size();i++)
      cout<<answrs[i]<<endl;
    return 0;
}
