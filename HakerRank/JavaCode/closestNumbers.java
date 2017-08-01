
import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long[] arv = new long[n];
        for(int i=0;i<n;i++){
            long v=(long)in.nextInt();
            arv[i]=v;
        }
        Arrays.sort(arv);
        long min=100000000;
        for(int i=1;i<n;i++){
            if(Math.abs(arv[i]-arv[i-1])<min){
                min=Math.abs(arv[i]-arv[i-1]);
            }
        }
        for(int i=1;i<n;i++){
            if(Math.abs(arv[i]-arv[i-1])==min){
                System.out.print(arv[i-1]+" "+arv[i]+" ");
            }
        }
    }
}
