import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


public class Solution {
    public static int compare(char[] st1,int is1,char[] st2,int is2){
        int res=0;
        int min=Math.min(st1.length-is1,st2.length-is2);
        for(int l=0;l<min;l++){
            if(st1[is1+l]>st2[is2+l]){
                return 1;
            }
        }
        return res;
    }
    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int T= in.nextInt();

        for(int t=0;t<T;t++) {
            String s1 = in.next();
            String s2 = in.next();
            s1+='z';
            s2+='z';
            char[] st1=s1.toCharArray();
            char[] st2=s2.toCharArray();
            int is1=0,is2=0;
            char[] s=new char[st1.length+st2.length];
            int idx=0;
            while(is1<s1.length()-1 && is2<s2.length()-1){
                char c1=st1[is1];
                char c2=st2[is2];
                if(c1=='z' || c2=='z'){
                    break;
                }
                if(c1!=c2){
                   if(c1>c2){
                       s[idx]=c2;
                       is2++;
                   }
                   else{
                       s[idx]=c1;
                       is1++;
                   }
                   idx++;
                }
                else{
                    int size=0;
                    if(is1>0) {
                        while (st1[is1 + size] == st1[is1 + size - 1] && st1[is1 + size] == st2[is2 + size]) {
                            size++;
                        }
                    }
                    for(int i=0;i<=2*size;i++) {
                        s[idx] = st1[is1+i];
                        idx++;
                    }
                    is1+=size;
                    is2+=size;
                    if(compare(st1,is1,st2,is2)>0){//st1>st2
                         s[idx] =st2[is2];
                         is2++;
                    }
                    else{
                         s[idx] =st1[is1];
                         is1++;
                    }
                    idx++;
                }
            }
            if(is1<s1.length()-1){
                while(is1<s1.length()-1){
                    s[idx]=st1[is1];
                    is1++;
                    idx++;
                }
            }
            if(is2<s2.length()-1){
                while(is2<s2.length()-1){
                    s[idx]=st2[is2];
                    is2++;
                    idx++;
                }
            }
            System.out.println(s);
        }
    }
}
