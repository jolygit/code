import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    Scanner in = new Scanner(System.in);
    int N=in.nextInt();
    Stack<Integer> st = new Stack<Integer>();
    ArrayList<Integer> arr=new ArrayList<Integer>();
    int max1=-1,max2=-1,max=-1;
      for(int rep=0;rep<N;rep++){
           int command=in.nextInt();
           int val;
           if(command==1){
             val=in.nextInt();
             st.push(val);
             if(val>max && max!=-2){
                max=val;
             }
           }
           else if(command==2){
               val=st.pop();
               if(val==max){
                   max=-2;
               }
           }
           else if(command==3){
            if(max==-2){
             for(Integer obj : st)
               {
                 if(max<obj){
                  max=obj;
                 }                 
               }
            }
           
           System.out.println(max);
           }
      }
     
    }
}
