import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    public static int compute(char c1,char c2,char[] arr){
        int cnt=0;
        boolean first=true;
        for(int i=0;i<arr.length;i++){
            if(first && arr[i]==c2){
               cnt=0;
               break;
            }
            else if(first && arr[i]==c1){
                first=false;
                cnt++;
            }
            else if(!first && arr[i]==c2){
                first=true;
                cnt++;
            }
            else if(!first && arr[i]==c1){
               cnt=0;
               break;
            }
        }
        return cnt;
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int len = in.nextInt();
        String s = in.next();
        char[] arr=s.toCharArray();
        char[] lets=new char[26];
        for(int i=0;i<26;i++){
            lets[i]=0;
        }
        for(char c:arr){
          lets[(int)c-97]+=1;
        }
        int max=0;
        for(int i=0;i<25;i++){
            if((int)lets[i]>0){
              for(int j=i+1;j<26;j++){
                if((int)lets[j]>0){
                    char c1=(char)(i+97);
                    char c2=(char)(j+97);
                    int cnt=compute(c1,c2,arr);
                    if(max<cnt){
                        max=cnt;
                    }
                    cnt=compute(c2,c1,arr);
                    if(max<cnt){
                        max=cnt;
                    }
                }
              }
            }
        }
       System.out.print(max);
    }
}
