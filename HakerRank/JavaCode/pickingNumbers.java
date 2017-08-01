/*
Given an array of integers, find and print the maximum number of integers you can select from the array such that the absolute difference between any two of the chosen integers is .

Input Format

The first line contains a single integer, , denoting the size of the array. 
The second line contains  space-separated integers describing the respective values of .

Constraints

The answer will be .
Output Format

A single integer denoting the maximum number of integers you can choose from the array such that the absolute difference between any two of the chosen integers is .

Sample Input 0

6
4 6 5 3 3 1
Sample Output 0

3
Explanation 0

We choose the following multiset of integers from the array: . Each pair in the multiset has an absolute difference  (i.e.,  and ), so we print the number of chosen integers, , as our answer.

Sample Input 1

6
1 2 2 3 1 2
Sample Output 1

5
Explanation 1

We choose the following multiset of integers from the array: . Each pair in the multiset has an absolute difference  (i.e., , , and ), so we print the number of chosen integers, , as our answer.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] a = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        Arrays.sort(a);
        int cnt=0,prev=1,next=1,pval=a[0],nval=a[1];
        for(int i=1;i<a.length;i++){
             if(a[i]==a[i-1]){
                 next++;
                 if(i==a.length-1 && next>cnt) {
                     cnt=next;
                 }
             }
            else{
                
                if(Math.abs(pval-nval)<2 && (prev+next)>cnt){
                   
                    cnt=prev+next;
                }
                else if(next>cnt){
                    cnt=next;
                }
                prev=next;
                next=1;
                if(i!=1){
                pval=nval;
                nval=a[i];
                }
            }
        }
        System.out.println(cnt);
    }
}
