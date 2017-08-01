

import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st = in.nextLine();
        boolean[] arr=new boolean[26];
        for(int i=0;i<st.length();i++){
            char c=st.charAt(i);
            if(c>=65 && c<=90){
                arr[c-65]=true;
            }
            else if(c>=97 && c<=122){
                arr[c-97]=true;
            }
        }
        boolean not=false;
        for(int i=0;i<26;i++){
            if(!arr[i]){
                not=true;
                break;
            }
        }
        if(not){
            System.out.println("not pangram");
        }
        else{
            System.out.println("pangram");
        }
    }
