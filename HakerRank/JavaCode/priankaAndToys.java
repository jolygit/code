import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        
         Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        long[] A = new long[n];
        for(int i=0;i<n;i++){
            int v = in.nextInt();
            A[i]=v;
        }
        Arrays.sort(A);
        int cnt=0,i=0;
        while(i<n){
            if(A[i]!=-1){
                cnt++;
                int j=i;
                long pval=A[i];
               while(i<n  && A[i]-pval<=4){
                   A[i]=-1;
                   i++;
               }
                if(i==j){
                    i++;
                }
            }
            else{
                i++;
            }
            
        }
       System.out.println(cnt) ;
    }
}
