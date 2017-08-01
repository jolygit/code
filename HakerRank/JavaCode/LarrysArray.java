/*
Larry has a permutation of  numbers, , whose unique elements range from  to  (i.e.: ). He wants  to be sorted, so he delegates the task of doing so to his robot. The robot can perform the following operation as many times as it wants:

Choose any  consecutive indices and rotate their elements in such a way that  rotates to , which rotates to , which rotates back to .
For example: if  and the robot rotates ,  becomes .

On a new line for each test case, print  if the robot can fully sort ; otherwise, print .

Input Format

The first line contains an integer, , the number of test cases. 
The  subsequent lines each describe a test case over  lines:

An integer, , denoting the size of .
 space-separated integers describing , where the  value describes element .
Constraints

Output Format

On a new line for each test case, print  if the robot can fully sort ; otherwise, print .

Sample Input

3
3
3 1 2
4
1 3 4 2
5
1 2 3 5 4
Sample Output

YES
YES
NO
Explanation

In the explanation below, the subscript of  denotes the number of operations performed.

Test Case 0: 
 
 is now sorted, so we print  on a new line.

Test Case 1: 
. 
. 
 is now sorted, so we print  on a new line.

Test Case 2:
No sequence of rotations will result in a sorted . Thus, we print  on a new line.
 */
import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
        int T=in.nextInt();
        for(int t=0;t<T;t++) {
            int N=in.nextInt();
            int[] arr=new int[N];
            for(int n=0;n<N;n++){
              int v=in.nextInt();
                arr[n]=v;
            }
            int cntP=0;
            for(int n=0;n<N-1;n++){
              for(int m=n+1;m<N;m++){
                if(arr[n]>arr[m]){
                    cntP++;
                }
              }
            }
            if(cntP%2==1){
                System.out.println("NO");
            }
            else{
                 System.out.println("YES");
            }
        }
    }
}
