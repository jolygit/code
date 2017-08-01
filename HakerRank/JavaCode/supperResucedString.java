import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static String super_reduced_string(String s){
        char[] arr=s.toCharArray();
        String st=new String();
        int i=0;
        boolean change=false;
        while(i<arr.length){
            if(i+1<arr.length){
                if(arr[i]==arr[i+1]){
                    i+=2;
                    change=true;
                }
                else{
                    st+=arr[i];
                    i++;
                }
            }
            else{
              st+=arr[i];
              i++;  
            }
        }
        if(st.length()==0){
            st="Empty String";
        }
        else if(change){
            st=super_reduced_string(st);
        }
        return st;
        // Complete this function
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        String result = super_reduced_string(s);
        System.out.println(result);
    }
}
