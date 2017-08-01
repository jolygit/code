
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n = in.nextInt();
        int k = in.nextInt();
        int[] x = new int[n];
        for(int x_i=0; x_i < n; x_i++){
            x[x_i] = in.nextInt();
        }
        Arrays.sort(x);
        int cnt=1;
        int lastRadio=0,leftBoundary=0;
        for(int i=1;i<x.length;i++){
            if(x[i]-k>x[lastRadio]){
                cnt++;
                lastRadio=i;
                leftBoundary=i;
            }
            else{
                if(x[i]-k<=x[leftBoundary]) {
                    lastRadio = i;
                }
            }
        }
        System.out.print(cnt);
    }
}
