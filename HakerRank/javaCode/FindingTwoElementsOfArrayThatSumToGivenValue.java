import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    Scanner in = new Scanner(System.in);
    int t=in.nextInt();
      for(int rep=0;rep<t;rep++){
           int m=in.nextInt();
           int n=in.nextInt();
           int[] c=new int[n];
           HashMap<Integer,Integer> hm=new HashMap<Integer,Integer>(); 
           for(int i=0;i<n;i++){
               c[i]=in.nextInt();
               hm.put(c[i],i);
           }
           for(int i=0;i<n;i++){
             int f=c[i];
             int s=m-f;
             if(hm.containsKey(s)){
                  int ind1=i+1;
                  int ind2=hm.get(s)+1;
                 if(ind1!=ind2){
                   if(ind1<ind2){
                     System.out.println(ind1+" "+ind2);
                   }
                   else{
                     System.out.println(ind2+" "+ind1);
                   }
                   break;
                 }
             }
           }
      }
    }
}
