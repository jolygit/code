/*An array, , is defined as follows:

 for , where  is the symbol for XOR
You must answer  questions. Each  question, is in the form , and the answer is  (the Xor-Sum of segment ).

Print the answer to each question.

Input Format

The first line contains  (the number of questions).
The  subsequent lines each contain two space separated integers,  and , respectively. Line contains  and .

Constraints 



Subtasks 
For  score: 

Output Format

On a new line for each test case , print the exclusive-or of 's elements in the inclusive range between indices and .

Sample Input

3
2 4
2 8
5 9
Sample Output

7
9
15
Explanation

The beginning of our array looks like this: 

Test Case 0:



Test Case 1:



Test Case 2:

*/
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
public static long G(long x){
    long a=x%8;
    if(a==0 || a==1){
        return x;
    }
    else if(a==2 || a==3){
        return 2;
    }
    else if(a==4 || a==5){
        return x+2;
    }
    else if(a==6 || a==7){
        return 0;
    }
return 0;
}
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int Q = in.nextInt();
        for(int a0 = 0; a0 < Q; a0++){
            long L = in.nextLong();
            long R = in.nextLong();
            long res=G(R)^G(L-1);
            System.out.println(res);
        }
    }
}
