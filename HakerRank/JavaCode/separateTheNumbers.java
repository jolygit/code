
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            char[] arr=s.toCharArray();
            if(arr[0]=='0' || arr.length==1){
                System.out.println("NO");
            }
            else{
                int size=1;
                int start=0;
                int n,next;
                boolean but=true;
                int startval=0;
                while(true){
                    String st=s.substring(start,start+size);
                    n=Integer.parseInt(st);
                    if(start==0) {
                        startval = n;
                    }
                    next=n+1;
                    String nxt=String.valueOf(next);
                    int l=nxt.length();
                    if(size>s.length()/2){
                        but=false;
                        break;
                    }
                    else if(start+size+l>s.length()){
                        start=0;
                        size++;
                    }
                    else{
                        start+=size;
                        boolean diff=false;
                        if(size!=l){
                            diff=true;
                        }
                        size=l;
                        nxt=s.substring(start,start+size);
                        int v=Integer.parseInt(nxt);
                        if(v!=next){
                            start=0;
                            if(!diff) {
                                size++;
                            }
                        }
                        else if(v==next && start+size==s.length()){
                            break;
                        }
                    }
                }
                if(but){
                    System.out.println("YES"+" "+startval);
                }
                else{
                    System.out.println("NO");
                }
            }
            // your code goes here
        }
    }
}

