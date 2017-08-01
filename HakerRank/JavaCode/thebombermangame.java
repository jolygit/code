import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt();
        int C = in.nextInt();
        long N = in.nextInt();
        int[][]  map=new int[R][C];
        for(int a0 = 0; a0 < R; a0++){
            String st=in.next();
            for(int i=0;i<st.length();i++){
                char c=st.charAt(i);
                if(c=='.') {
                    map[a0][i]=0;
                }
                else{
                    map[a0][i]=1;
                }
            }
        }
        int[][] map3=new int[R][C];
        int[][] map5=new int[R][C];
        int[][] map2=new int[R][C];
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                map3[r][c]=map[r][c];
                map2[r][c]=1;
            }
        }
        plant(map3,R,C);
        explode(map3,R,C);
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                map5[r][c]=map3[r][c];
            }
        }
        plant(map5,R,C);
        explode(map5,R,C);
        long n=N%4;
        if(N==1){
            
        }
        else if(n==0 || n==2){
            map=map2;
        }
        else if(n==3){
            map=map3;
        }
        else if(n==1){
            map=map5;
        }
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                if(map[r][c]==0) {
                    System.out.print(".");
                }
                else{
                    System.out.print("O");
                }
            }
            System.out.println();
        }
    }
    public static void explode(int[][] map,int R,int C){
        int[][] mapcpy=new int[R][C];
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                mapcpy[r][c]=map[r][c];
            }
        }
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                if(mapcpy[r][c]==2) {
                    map[r][c]=0;
                    if(r>0) {
                        map[r-1][c] = 0;
                    }
                    if(r<R-1) {
                        map[r+1][c] = 0;
                    }
                    if(c>0) {
                        map[r][c-1] = 0;
                    }
                    if(c<C-1) {
                        map[r][c+1] = 0;
                    }
                }
            }
        }
    }
    public static void plant(int[][] map,int R,int C){
        for(int r=0;r<R;r++){
            for(int c=0;c<C;c++){
                map[r][c]++;
            }
        }
    }
}
