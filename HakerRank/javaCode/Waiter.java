/*You are a waiter at a party. There are  stacked plates on pile . Each plate has a number written on it. Then there will be  iterations. In -th iteration, you start picking up the plates in  from the top one by one and check whether the number written on the plate is divisible by the -th prime. If the number is divisible, you stack that plate on pile . Otherwise, you stack that plate on pile . After  iterations, plates can only be on pile , . Output numbers on these plates from top to bottom of each piles in order of , .

Input Format

The first line contains two space separated integers,  and . 
The next line contains  space separated integers representing the initial pile of plates, i.e., . The leftmost value represents the bottom plate of the pile.

Constraints

 
 

Output Format

Output  lines. Each line contains a number written on the plate. Printing should be done in the order defined above.*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    public static int popCount(int n) {
        n -= (n >>> 1) & 0x55555555;
        n = ((n >>> 2) & 0x33333333) + (n & 0x33333333);
        n = ((n >> 4) & 0x0F0F0F0F) + (n & 0x0F0F0F0F);
        return (n * 0x01010101) >> 24;
    }

    // Speed up counting by counting the primes per
// array slot and not individually. This yields
// another factor of about 1.24 or so. Found this code online
    public static int nthPrime(int n) {
        if (n < 2) return 2;
        if (n == 2) return 3;
        if (n == 3) return 5;
        int limit, root, count = 2;
        limit = (int)(n*(Math.log(n) + Math.log(Math.log(n)))) + 3;
        root = (int)Math.sqrt(limit);
        switch(limit%6) {
            case 0:
                limit = 2*(limit/6) - 1;
                break;
            case 5:
                limit = 2*(limit/6) + 1;
                break;
            default:
                limit = 2*(limit/6);
        }
        switch(root%6) {
            case 0:
                root = 2*(root/6) - 1;
                break;
            case 5:
                root = 2*(root/6) + 1;
                break;
            default:
                root = 2*(root/6);
        }
        int dim = (limit+31) >> 5;
        int[] sieve = new int[dim];
        for(int i = 0; i < root; ++i) {
            if ((sieve[i >> 5] & (1 << (i&31))) == 0) {
                int start, s1, s2;
                if ((i & 1) == 1) {
                    start = i*(3*i+8)+4;
                    s1 = 4*i+5;
                    s2 = 2*i+3;
                } else {
                    start = i*(3*i+10)+7;
                    s1 = 2*i+3;
                    s2 = 4*i+7;
                }
                for(int j = start; j < limit; j += s2) {
                    sieve[j >> 5] |= 1 << (j&31);
                    j += s1;
                    if (j >= limit) break;
                    sieve[j >> 5] |= 1 << (j&31);
                }
            }
        }
        int i;
        for(i = 0; count < n; ++i) {
            count += popCount(~sieve[i]);
        }
        --i;
        int mask = ~sieve[i];
        int p;
        for(p = 31; count >= n; --p) {
            count -= (mask >> p) & 1;
        }
        return 3*(p+(i<<5))+7+(p&1);
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    int primes[]=new int[1201];
    for (int i=0;i<1201;i++){
       primes[i]=nthPrime(i+1);
    }
       Scanner in = new Scanner(System.in);
       int N = in.nextInt();
       int Q = in.nextInt();
       //int A0[]=new int[N];
       Stack<Integer> A0 = new Stack<Integer>();
       Stack<Integer> A1 = new Stack<Integer>();
       Stack<Integer> Aleft = new Stack<Integer>();
       for(int i=0;i<N;i++){
           A0.push(in.nextInt());
       }
       int cnt=0; // this is to alternate stacks A0 and A1
       for(int q=0;q<Q;q++){
         int val;
        Stack<Integer> B = new Stack<Integer>();
         if(cnt%2==0){
           while(!A0.empty()){
             val=A0.pop();             
             if(val%primes[q] == 0){
               B.push(val);
             }
             else{
               A1.push(val);  
             }
           }           
         }
         else{
           while(!A1.empty()){
             val=A1.pop();             
             if(val%primes[q] == 0){
                B.push(val);
             }
             else{
               A0.push(val);  
             }
           }  
         }
       while(!B.empty()){
            System.out.println(B.pop());
       }
       cnt++;
       }
       if(cnt%2==1){
        while(!A1.empty()){
            System.out.println(A1.pop());
        }
       }
       else{
        while(!A0.empty()){
            System.out.println(A0.pop());
        } 
       }
    }
}
