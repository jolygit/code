import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] h = new int[26];
        for(int h_i=0; h_i < 26; h_i++){
            h[h_i] = in.nextInt();
        }
        String word = in.next();
        HashMap<Character,Integer> alpha=new HashMap<Character,Integer>();
         alpha.put('a',0);
        alpha.put('b',1);
        alpha.put('c',2);
        alpha.put('d',3);
        alpha.put('e',4);
        alpha.put('f',5);
        alpha.put('g',6);
        alpha.put('h',7);
        alpha.put('i',8);
        alpha.put('j',9);
        alpha.put('k',10);
        alpha.put('l',11);
        alpha.put('m',12);
        alpha.put('n',13);
        alpha.put('o',14);
        alpha.put('p',15);
        alpha.put('q',16);
        alpha.put('r',17);
        alpha.put('s',18);
        alpha.put('t',19);
        alpha.put('u',20);
        alpha.put('v',21);
        alpha.put('w',22);
        alpha.put('x',23);
        alpha.put('y',24);
        alpha.put('z',25);
	int max=0;
       for(int i=0;i<word.length();i++){
           char l=word.charAt(i);
           int index=alpha.get(l);
           int hm=h[index];
           if(hm>max){
               max=hm;
           }
       }
       System.out.println(word.length()*max) ;
    }
}
