#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
    freopen("c:/Users/alex/code/HackerRank/JavaCode/input.txt", "r", stdin);
    int T;
    cin >> T;
    for(int t=0;t<T;t++){
        long N;
        cin >> N;
        long n=0;
        long arr[N];
        while(n<N){
            long val;
            cin >> val;
            arr[n]=val;
            n++;
        }
        long res=0;
        long val[N];
        for(int i=0;i<N;i++){
            val[i]=0;
        }
        val[0]=N;
        long inc=N-2;
        for(int i=1;i<N;i++){
            val[i]=val[i-1]+inc;
             inc-=2;
        }
        for(int i=0;i<N;i++){
           if(val[i]%2==1){
               res^=arr[i];
           }
        }
        cout<<res<<endl;
    }
    return 0;
}
