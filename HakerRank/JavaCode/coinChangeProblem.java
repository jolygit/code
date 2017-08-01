/*
You have  types of coins available in infinite quantities where the value of each coin is given in the array . Can you determine the number of ways of making change for  units using the given types of coins? For example, if , and , we can make change for  units in three ways: , , and .

Given , , and , print the number of ways to make change for  units using any number of coins having the values given in .

Input Format

The first line contains two space-separated integers describing the respective values of  and . 
The second line contains  space-separated integers describing the respective values of  (the list of distinct coins available in infinite amounts).

Constraints

Each  is guaranteed to be distinct.
Hints

Solve overlapping subproblems using Dynamic Programming (DP): 
You can solve this problem recursively but will not pass all the test cases without optimizing to eliminate the overlapping subproblems. Think of a way to store and reference previously computed solutions to avoid solving the same subproblem multiple times.
Consider the degenerate cases: 
How many ways can you make change for  cents?
How many ways can you make change for  cents if you have no coins?
If you're having trouble defining your solutions store, then think about it in terms of the base case .
The answer may be larger than a -bit integer.
Output Format

Print a long integer denoting the number of ways we can get a sum of  from the given infinite supply of  types of coins.

Sample Input 0
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
           Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //  Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int M = in.nextInt();
        int[] c=new int[M];
        long[][] ways=new long[N+1][M];
        for (int i = 0; i < M; i++) {
          c[i]=in.nextInt();
          ways[0][i]=1;
        }
        Arrays.sort(c);
        for(int n=1;n<N+1;n++){
            int k=c[0];
            if(n-k>=0){
                ways[n][0]=ways[n-k][0];
            }
            else{
                ways[n][0]=0;
            }
        }
        for(int n=1;n<N+1;n++){
            for (int m = 1; m < M; m++) {
                int k=c[m];
                if(n-k>=0){
                    ways[n][m]=ways[n-k][m]+ways[n][m-1];
                }
                else{
                    ways[n][m]=ways[n][m-1];
                }
            }
        }
        System.out.println(ways[N][M-1]);
    }
}
