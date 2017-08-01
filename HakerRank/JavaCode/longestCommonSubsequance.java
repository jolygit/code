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
        int N = in.nextInt();
        int[] a1 = new int[(int) N];
        int M = in.nextInt();
        int[] a2 = new int[(int) M];
        int[][] c=new int[N+1][M+1];
        int[][] b=new int[N+1][M+1];//2 =left 1=up 3 =diagonal
        for (int n = 0; n < N; n++) {
            int el = in.nextInt();
            a1[n]=el;
            c[n][0]=0;
        }
        for (int m = 0; m < M; m++) {
            int el = in.nextInt();
            a2[m]=el;
            c[0][m]=0;
        }
        for (int n = 0; n < N; n++) {
            for (int m = 0; m < M; m++) {
               if(a1[n]==a2[m]){
                   c[n+1][m+1]=c[n][m]+1;
                   b[n+1][m+1]=3;
               }
               else if(c[n+1][m]>c[n][m+1]) {
                   c[n+1][m+1]=c[n+1][m];
                   b[n+1][m+1]=2;
               }
               else{
                   c[n+1][m+1]=c[n][m+1];
                   b[n+1][m+1]=1;
               }
            }
        }
        int i=N;
        int j=M;
        /*for(int n=0;n<N+1;n++){
            for(int m=0;m<M+1;m++) {
             System.out.print(b[n][m]+" ");
            }
            System.out.println();
        }*/
        print(i,j,b,a1);
        //System.out.println(c[N][M]);
    }
    static void print(int i,int j,int[][] b,int[] a1){
        if(i>0 && j>0) {
            if (b[i][j] == 3) {
                print(i - 1, j - 1,b,a1);
                System.out.print(a1[i-1]+" ");
            } else if (b[i][j] == 2) {
                print(i , j-1,b,a1);
            } else {
                print(i-1, j ,b,a1);
            }
        }
    }
}
