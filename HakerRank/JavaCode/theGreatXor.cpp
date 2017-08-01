/*
Given a long integer, , count the number of values of  satisfying the following conditions:

where  and  are long integers and  is the bitwise XOR operator.

You are given  queries, and each query is in the form of a long integer denoting . For each query, print the total number of values of  satisfying the conditions above on a new line.

Input Format

The first line contains an integer, , denoting the number of queries. 
Each of the  subsequent lines contains a long integer describing the value of  for a query.

Constraints

Subtasks

For  of the maximum score:

Output Format

For each query, print the number of values of  satisfying the given conditions on a new line.

Sample Input 0

2
2
10
Sample Output 0

1
5
Explanation 0
 */
#include <bits/stdc++.h>

using namespace std;

 long theGreatXor(long x){
    int index=0;
     long one=1,cnt=0;
    //vector<int> inds;
    while(x){
        if((x&one)!=one){
          // inds.push_back(index) ;
           cnt+=(one<<index);
        }
         x>>=1;
         index++;
    }
   return cnt;
}

int main() {
    int q;
    cin >> q;
    for(int a0 = 0; a0 < q; a0++){
        long x;
        cin >> x;
        long result = theGreatXor(x);
        cout << result << endl;
    }
    return 0;
}
