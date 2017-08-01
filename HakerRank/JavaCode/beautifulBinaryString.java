import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int minSteps(int n, String B){
        char[] arr=new char[B.length()];
        for(int i=0;i<B.length();i++){
            arr[i]=B.charAt(i);
        }
        if(n<=3){
            return 0;
        }
        int cnt=0;
        for(int i=2;i<n;i++){
            char c0=arr[i-2];
            char c1=arr[i-1];
            char c2=arr[i]; 
            if(c0=='0' && c1=='1' && c2=='0'){
                cnt++;
                arr[i]='1';
            }
        }
        return cnt;
        // Complete this function
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String B = in.next();
        int result = minSteps(n, B);
        System.out.println(result);
    }
}
