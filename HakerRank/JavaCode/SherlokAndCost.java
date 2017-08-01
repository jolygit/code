/*
Array A contains the elements, A1,A2...AN. And array B contains the elements, B1,B2...BN. There is a relationship between Ai and Bi, ∀ 1 ≤ i ≤ N, i.e., 
any element Ai lies between 1 and Bi.

Let the cost S of an array A be defined as:

You have to print the largest possible value of S.

Input Format

The first line contains, T, the number of test cases. Each test case contains an integer, N, in first line. The second line of each test case contains N integers that denote the array B.

Constraints

1 ≤ T ≤ 20 
1 ≤ N ≤ 105 
1 ≤ Bi ≤ 100

Output Format

For each test case, print the required answer in one line.

Sample Input

1
5
10 1 10 1 10
Sample Output

36
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
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            int[] B = new int[N];
            int[] A = new int[N];
            long[] L=new long[N];
            long[] H=new long[N];
            for (int n = 0; n < N; n++) {
                int el = in.nextInt();
                B[n] = el;
                if(n==0){
                    L[0]=0;
                    H[0]=0;
                }
                else{
                    L[n]=Math.max(L[n-1],H[n-1]+Math.abs(B[n-1]-1));
                    H[n]=Math.max(H[n-1],L[n-1]+Math.abs(el-1));
                }
            }
            System.out.println(Math.max(L[N-1],H[N-1]));
        }
    }

}
