/*
Louise and Richard play a game. They have a counter set to . Louise gets the first turn and the turns alternate thereafter. In the game, they perform the following operations.

If  is not a power of , reduce the counter by the largest power of  less than .
If  is a power of , reduce the counter by half of .
The resultant value is the new  which is again used for subsequent operations.
The game ends when the counter reduces to 1, i.e., , and the last person to make a valid move wins.

Given , your task is to find the winner of the game.

Update If they set counter to , Richard wins, because its Louise' turn and she cannot make a move.

Input Format

The first line contains an integer , the number of testcases. 
 lines follow. Each line contains , the initial number set in the counter.

Constraints

Output Format

For each test case, print the winner's name in a new line. So if Louise wins the game, print "Louise". Otherwise, print "Richard". (Quotes are for clarity)

Sample Input

1
6
Sample Output

Richard
Explanation

As  is not a power of , Louise reduces the largest power of  less than  i.e., , and hence the counter reduces to .
As  is a power of , Richard reduces the counter by half of  i.e., . Hence the counter reduces to .
As we reach the terminating condition with , Richard wins the game.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int T = in.nextInt();
        in.nextLine();
        for(int t=0;t<T;t++){
            String s= in.nextLine();
            BigInteger N=new BigInteger(s);
            BigInteger c=new BigInteger("0");
            c=N;
            int cnt=0;
            BigInteger one=new BigInteger("1");
            BigInteger zero=new BigInteger("0");
            while(true) {
                BigInteger res = c.and(c.subtract(one));
                if (res.equals(zero)) {
                    c=c.shiftRight(1);
                }
                else{
                    c=subtract(c);
                }
                cnt++;
                if(c.equals(one)){
                    break;
                }
            }
            String str=(cnt%2==0)?"Richard":"Louise";
            System.out.println(str);
        }
    }
    static BigInteger subtract(BigInteger c){
        int cnt=0;
        BigInteger copy=new BigInteger("0");
        copy=c;
        BigInteger one=new BigInteger("1");
        while (!c.equals(one)){
            c=c.shiftRight(1);
            cnt++;
        }
        BigInteger p=new BigInteger("2");
        p=p.shiftLeft(cnt-1);
        return copy.subtract(p);
    }
}
