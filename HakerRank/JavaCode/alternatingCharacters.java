import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int alternatingCharacters(String s,boolean fstA){
        char[] arr=s.toCharArray();
        int cnt=0;
        
        for(int i=0;i<arr.length;i++){
            if(fstA && arr[i]=='B'){
                cnt++;
            }
            else if(fstA && arr[i]=='A'){
                fstA=false;
            }
            else if(!fstA && arr[i]=='B'){
                fstA=true;
            }
            else if(!fstA && arr[i]=='A'){
                cnt++;
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
            int resultA = alternatingCharacters(s,true);
            int resultB = alternatingCharacters(s,false);
            System.out.println(resultA<resultB?resultA:resultB);
        }
    }
}
