import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int commonChild(String s1, String s2){
        char[] st1=s1.toCharArray();
        char[] st2=s2.toCharArray();
        int N=st1.length;
        int[][] map=new int[N+1][N+1];
        for (int i=0;i<N+1;i++){
            map[0][i]=0;
            map[i][0]=0;
        }
        for (int i=1;i<N+1;i++){
            for (int j=1;j<N+1;j++){
             if(st1[i-1]==st2[j-1]){
                 map[i][j]=map[i-1][j-1]+1;
             }
             else if(map[i][j-1]>map[i-1][j]){
                 map[i][j]=map[i][j-1];
             }
             else{
                 map[i][j]=map[i-1][j];
             }
            }
        }
        // Complete this function
        return map[N][N];
    }

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s1 = in.next();
        String s2 = in.next();
        int result = commonChild(s1, s2);
        System.out.println(result);
    }
}
