import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    static int minNumChanges(char[] st){
        int num=0;
        for(int i=0;i<st.length/2;i++){
            if(st[i]!=st[st.length-i-1]){
                num++;
            }
        }
        return num;
    }
    static String richieRich(String s, int n, int k){
        char[] st=new char[s.length()];
        for(int i=0;i<s.length();i++){
            st[i]=s.charAt(i);
        }
        String poly="";
        if(n==1 || k>=n){
            for(int i=0;i<n;i++){
                poly+='9';
            }
            return poly;
        }
        int min=minNumChanges(st);
        
        if(k<min){
            return "-1";
        }
        else{
            int i=0;
            while(true){
               
               if(k>=min+2){
                 if(st[i]!=st[st.length-i-1]){
                     min--;
                 }
                 if(st[i]!='9'){
                   st[i]='9'; 
                   k--;
                 }
                 if(st[st.length-i-1]!='9'){
                    st[st.length-i-1]='9';
                    k--;
                 }
               }
               else if(k==min+1){
                 if(st[i]!=st[st.length-i-1]){
                     min--;
                    if(st[i]!='9'){
                      st[i]='9'; 
                      k--;
                    }
                    if(st[st.length-i-1]!='9'){
                      st[st.length-i-1]='9';
                      k--;
                    }
                 } 
               }
               i++;
               if(k==min || i==st.length/2){
                    palindromize(st,k);
                    break;
               }
           }
           for(i=0;i<st.length;i++){
                poly+=st[i];
           }
         }
        return poly;
        // Complete this function
    }
    //this function assumes k==min
    static void palindromize(char[] st,int k){
        for(int i=0;i<st.length/2;i++){
             if(st[i]!=st[st.length-i-1]){
                if(st[i]>st[st.length-i-1]){
                    st[st.length-i-1]=st[i];
                }
                else{
                    st[i]=st[st.length-i-1]; 
                }
             }
        }
        if(k==1){
            st[st.length/2]='9';
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        String s = in.next();
        String result = richieRich(s, n, k);
        System.out.println(result);
    }
}
