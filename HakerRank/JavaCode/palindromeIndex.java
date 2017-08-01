/*
 Times out on the last test case!!!
Given a string, , of lowercase letters, determine the index of the character whose removal will make  a palindrome. If  is already a palindrome or no such character exists, then print . There will always be a valid solution, and any correct answer is acceptable. For example, if  "bcbc", we can either remove 'b' at index  or 'c' at index .
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int palindromeIndex(String s){
        char[] arr=new char[s.length()];
        for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            arr[i]=c;
        }
        boolean tst=true;
        
        int index=-1;
        for(int i=0;i<s.length();i++){
            if(arr[i]!=arr[s.length()-i-1]){
               tst=false;
                break;
            }
        }
        if(tst){
            return -1;
        }
        else{
         for(int i=0;i<s.length();i++){
           tst=isPoly(arr,i);
            if(tst){
                index=i;
               break;
            }
         }
            if(index>-1){
                return index;
            }
            else{
                return -1;
            }
        }
        // Complete this function
    }
    static boolean isPoly(char[] arr,int index){
       
            int l=0;
            int h=arr.length-1;
            if(l==index){
                l++;
            }
            if(h==index){
                h--;
            }
            boolean isp=true;
            while(l<h){
                if(arr[l]!=arr[h]){
                    isp=false;
                    break;
                }
                l++;
                h--;
                if(l==index){
                 l++;
                }
                if(h==index){
                 h--;
                }
            }
      return isp;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            int result = palindromeIndex(s);
            System.out.println(result);
        }
    }
}
