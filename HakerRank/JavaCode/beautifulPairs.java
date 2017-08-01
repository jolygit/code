import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //  Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        long[] A = new long[1000];
        long[] B = new long[1000];
        for(int i=0;i<n;i++){
            int v = in.nextInt();
            A[v-1]++;
        }
        for(int i=0;i<n;i++){
            int v = in.nextInt();
            B[v-1]++;
        }
        for(int i=0;i<A.length;i++){
            System.out.print(A[i]+" ");
        }
        System.out.println();
        for(int i=0;i<A.length;i++){
            System.out.print(B[i]+" ");
        }
        System.out.println();
        int cnt=0;
        int found=1;
        for(int i=0;i<A.length;i++){
            if(A[i]!=0 && B[i]!=0){
                cnt+=Math.min(A[i],B[i]);
            }
            if( A[i]<B[i] && Math.abs(found)<2){
               found*=2;
            }
            if( A[i]>B[i] && found>0){
                found*=-1;
            }
        }
        if(found==-2) {
            System.out.print(cnt + 1);
        }

        else {
            System.out.print(cnt-1);
        }
    }
}
