import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        Set<Integer> vals=new HashSet<>();
        char pr=' ';
        int cnt=0;
        for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            int val=(int)c-96;
            if(c==pr){
                cnt+=val;
            }
            else{
                cnt=val;
            }
            pr=c;
            vals.add(cnt);
        }
        int n = in.nextInt();
        for(int a0 = 0; a0 < n; a0++){
            int x = in.nextInt();
            if(vals.contains(x)){
                System.out.println("Yes");
            }
            else{
                System.out.println("No");
            }
            // your code goes here
        }
    }
}
