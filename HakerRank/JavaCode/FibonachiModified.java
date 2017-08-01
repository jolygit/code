/*
We define a modified Fibonacci sequence using the following definition:

Given terms  and  where , term  is computed using the following relation:
For example, if term  and , term , term , term , and so on.

Given three integers, , , and , compute and print term  of a modified Fibonacci sequence.

Note: The value of  may far exceed the range of a -bit integer. Many submission languages have libraries that can handle such large results but, for those that don't (e.g., C++), you will need to be more creative in your solution to compensate for the limitations of your chosen submission language.

Input Format

A single line of three space-separated integers describing the respective values of , , and .

Constraints

 may far exceed the range of a -bit integer.
Output Format

Print a single integer denoting the value of term  in the modified Fibonacci sequence where the first two terms are  and .

Sample Input

0 1 5
Sample Output

5
Explanation

The first two terms of the sequence are  and , which gives us a modified Fibonacci sequence of . Because , we print term , which is .
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
        int t1 = in.nextInt();
        int t2 = in.nextInt();
        int N = in.nextInt();
        ArrayList<BigInteger> fn=new ArrayList<>();
        BigInteger bt1=new BigInteger(String.valueOf(t1));
        BigInteger bt2=new BigInteger(String.valueOf(t2));
        fn.add(bt1);
        fn.add(bt2);
        for (int n = 2; n < N; n++) {
            BigInteger btp=fn.get(n-1);
            BigInteger btpp=fn.get(n-2);
            BigInteger sq=btp.multiply(btp);
            BigInteger cv=btpp.add(sq);
            fn.add(cv);
        }
             System.out.println(fn.get(N-1).toString());
    }

}
