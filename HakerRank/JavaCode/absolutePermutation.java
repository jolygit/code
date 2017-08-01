/*
We define  to be a permutation of the first  natural numbers in the range . Let  denote the position of  in permutation  (please use -based indexing).

 is considered to be an absolute permutation if  holds true for every .

Given  and , print the lexicographically smallest absolute permutation, ; if no absolute permutation exists, print -1.

Input Format

The first line of input contains a single integer, , denoting the number of test cases. 
Each of the  subsequent lines contains  space-separated integers describing the respective  and  values for a test case.

Constraints

Output Format

On a new line for each test case, print the lexicographically smallest absolute permutation; if no absolute permutation exists, print -1.

Sample Input

3
2 1
3 0
3 2
Sample Output

2 1
1 2 3
-1
Explanation

Test Case 0:

perm.png

Test Case 1:

perm(1).png

Test Case 2: 
No absolute permutation exists, so we print -1 on a new line.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            int k = in.nextInt();
            Set<Integer> set=new HashSet<Integer>();
            boolean works=true;
            int[] perm=new int[n];
            for(int i=1;i<=n;i++){
               
                if(i-k>0 && !set.contains(i-k)){
                    set.add(i-k);
                    perm[i-k-1]=i;
                }
                else if(i+k<=n && !set.contains(i+k)){
                    set.add(i+k);
                    perm[i+k-1]=i;
                }
                else{
                    works=false;
                }
            }
            if(works){
                for(int i=0;i<n;i++){
                System.out.print(perm[i]+" ");
                }
                System.out.println();
            }
            
            else{
                System.out.println(-1);
            }
        }
    }
}
