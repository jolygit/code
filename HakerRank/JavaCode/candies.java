/*
Alice is a kindergarten teacher. She wants to give some candies to the children in her class.  All the children sit in a line (their positions are fixed), and each  of them has a rating score according to his or her performance in the class.  Alice wants to give at least 1 candy to each child. If two children sit next to each other, then the one with the higher rating must get more candies. Alice wants to save money, so she needs to minimize the total number of candies given to the children.

Input Format

The first line of the input is an integer , the number of children in Alice's class. Each of the following  lines contains an integer that indicates the rating of each child.

Constraints

Output Format

Output a single line containing the minimum number of candies Alice must buy.

Sample Input

3  
1  
2  
2
Sample Output

4
Explanation

Here 1, 2, 2 is the rating. Note that when two children have equal rating, they are allowed to have different number of candies. Hence optimal distribution will be 1, 2, 1.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
                      Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int[] R=new int[N];
        long[] K=new long[N];
        for (int n = 0; n < N; n++) {
            int el = in.nextInt();
            R[n] = el;
            if(n==0){
                K[0]=1;
            }
            else{
                if(R[n]<=R[n-1]){
                    K[n]=1;
                }
                else{
                    K[n]=K[n-1]+1;
                }
            }
        }
        long sum=0;
        sum+=K[N-1];
        for (int n = N-2; n >=0; n--) {
            int el=R[n];
            if(R[n]>R[n+1] && K[n]<=K[n+1]){
                K[n]=K[n+1]+1;
            }
            sum+=K[n];
        }
        System.out.println(sum);
    }

}
