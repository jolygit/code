#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
int T;
cin >> T;
for(int t=0;t<T;t++){
    long a,b;
    cin >> a;
    cin >> b;
    long p=a;
    long res=a;
    while(p<b){
        p++;
        res&=p;
    }
cout<<res<<endl;
}
    /* Enter your code here. Read input from STDIN. Print output to STDOUT */   
    return 0;
}
