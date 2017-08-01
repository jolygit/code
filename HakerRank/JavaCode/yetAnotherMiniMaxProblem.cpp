/*
  /*
You are given  non-negative integers, . We define the score for some permutation () of length to be the maximum of  for .

Find the permutation with the minimum possible score and print its score.

Note:  is the exclusive-OR (XOR) operator.

Input Format

The first line contains single integer, , denoting the number of integers. 
The second line contains  space-separated integers, , describing the respective integers.

Constraints

Output Format

Print a single integer denoting the minimum possible score.

Sample Input 0

4
1 2 3 4
Sample Output 0

5
Sample Input 1

3
1 2 3
Sample Output 1

2
Explanation

Sample Case 0: 
The permutation with the minimum score is : 
 
 

Because the permutation's score is the maximum of these values, we print  on a new line.

Sample Case 1: 
The permutation with the minimum score is : 
 

Because the permutation's score is the maximum of these values, we print  on a new line.
 */
#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
     freopen("c:/Users/alex/code/HackerRank/JavaCode/input.txt", "r", stdin);
    int N;
    cin >> N;
    unsigned long arr[N];
    long st[N];
    long fn[N];
    bool finishSama=true;
    long maxV=0;
    int mi=-1;
    long maxStart=0;
    for(int t=0;t<N;t++){
        unsigned long v,one=1;
        cin >> v;
        arr[t]=v;
        long s=-1,f=-1;
        long cnt=0;
        while(v){
            if((v&one)==1){
                if(s==-1){
                    s=cnt;
                    st[t]=s;
                    f=cnt;
                    fn[t]=f;
                }
                else{
                    f=cnt;
                    fn[t]=f;
                }
            }
            else{
                s=-1;
                f=-1;
            }
            cnt++;
            v>>=1;
        }
        if(t>0){
            if(fn[t]!=fn[t-1]){
                finishSama=false;
            }
        }
        if(arr[t]>maxV){
            maxV=arr[t];
            mi=t;
        }
        if(st[t]>maxStart){
            maxStart=st[t];
        }
    }
    vector<unsigned long> ones,zeros;
    if(maxV==0){
        cout<<0;
    }
    else if(finishSama){
        for(int i=0;i<N;i++){
            if(st[i]==maxStart){
                zeros.push_back(arr[i]);
            }else{
                ones.push_back(arr[i]);
            }
        }
        unsigned long res=100000000000;
        for(int one=0;one<ones.size();one++){
            for(int zero=0;zero<zeros.size();zero++){
                if((ones[one]^zeros[zero])<res){
                    res=ones[one]^zeros[zero];
                }
            }
        }
        cout<<res;
    } else{
        for(int i=0;i<N;i++){
            if(fn[i]==fn[mi]){
                ones.push_back(arr[i]);
            }else{
                zeros.push_back(arr[i]);
            }
        }
        unsigned long res=100000000000;
        for(int one=0;one<ones.size();one++){
            for(int zero=0;zero<zeros.size();zero++){
                if((ones[one]^zeros[zero])<res){
                    res=ones[one]^zeros[zero];
                }
            }
        }
        cout<<res;
    }
    return 0;
}

