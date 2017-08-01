import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int theLoveLetterMystery(String s){
        char[] arr=s.toCharArray();
        int cnt=0;
        for(int i=0;i<arr.length/2;i++){
            char c1=arr[i];
            char c2=arr[arr.length-i-1];
            if(c1!=c2){
                cnt+=Math.abs((int)(c1-c2));
            }
        }
        return cnt;
        // Complete this function
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            int result = theLoveLetterMystery(s);
            System.out.println(result);
        }
    }
}
