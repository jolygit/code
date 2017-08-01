import java.io.*;
import java.util.*;

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
        int[] arv = new int[n];
        ArrayList<ArrayList<String>> arrays=new ArrayList<>(100);
        for(int i=0;i<100;i++){
            ArrayList<String> ar=new ArrayList<>();
            arrays.add(ar);
        }
        for(int i=0;i<n;i++){
            int v=in.nextInt();
            String s=in.next();
            if(i<n/2){
                s="-";
            }
            ArrayList<String> ar=arrays.get(v);
            ar.add(s);
        }
        for(int i=0;i<100;i++){
            ArrayList<String> ar=arrays.get(i);
            if(ar.size()>0){
                for(String ss:ar){
                    System.out.print(ss+" ");
                }
            }
        }
    }
}
