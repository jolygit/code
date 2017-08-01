
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
        String[] unsorted = new String[n];
        int[] len=new int[n];
        for(int unsorted_i=0; unsorted_i < n; unsorted_i++){
            unsorted[unsorted_i] = in.next();
            len[unsorted_i]=unsorted[unsorted_i].length();
        }
        Arrays.sort(unsorted,new Comparator<String>() {
            @Override
            public int compare(String o1,String o2) {
                if(o1.length()!=o2.length()) {
                    return o1.length() - o2.length();
                }
                else{
                    return o1.compareTo(o2);
                }
            }
        });
        for(String s:unsorted){
            System.out.println(s);
        }
        // your code goes here
    }
}
