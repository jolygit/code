#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;
int check(int num,int lessThen,int exp){
    int cnt=0;
    if(num==0)
        return 1;
    else if(lessThen==0 || num<0) {
        return 0;
    }
    else{
       for(int val=lessThen;val>0;val--) {
           int numWays=check(num - pow(val,exp),val-1,exp);
           cnt+=numWays;
       }
    }
    return cnt;
}

int main() {
     freopen("c:/Users/alex/code/HackerRank/JavaCode/input.txt", "r", stdin);
    int x;
    cin >> x;
    int n;
    cin >> n;
    double rt=pow(x,1.0/n);
    int rnd=(int)rt;
    int cnt=0;
    for(int i=rnd;i>0;i--){
        int res=1;
        for(int j=0;j<n;j++){
            res*=i;
        }
        cnt+=check(x-res,i-1,n);
    }
    cout<<cnt;
    return 0;
}
