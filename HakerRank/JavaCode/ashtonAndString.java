import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class pair {
    pair(int f,int s){
        first=f;
        second=s;
    }
    public int first;
    public int second;
}
public class Solution {

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int T= in.nextInt();
        for(int t=0;t<T;t++) {
            String s = in.next();
            int k =in.nextInt();
            ArrayList<pair> vv=new ArrayList<>();
            int i=0,it=0;
            for(char c='a';c<='z' && k>0;c++){
                vv.clear();
                for(int l=0;l<s.length();l++){
                    if(s.charAt(l)==c){
                        pair pr=new pair(l,s.length()-l);
                        vv.add(pr);
                    }
                }
                Collections.sort(vv, new Comparator<pair>() {
                    @Override
                    public int compare(pair o1,pair o2) {
                        String s1=s.substring(o1.first,s.length());
                        String s2=s.substring(o2.first,s.length());
                        return s1.compareTo(s2);
                    }
                });
                for(it=0;it<vv.size() && k>0;it++){
                    i=1;
                    pair p2=vv.get(it);
                    if(it>0){
                        pair p1=vv.get(it-1);
                        String s1=s.substring(p1.first,s.length());
                        String s2=s.substring(p2.first,s.length());
                        while(i<=p1.second && i<=p2.second && s1.charAt(i-1)==s2.charAt(i-1)){
                            i++;
                        }
                    }
                    while(k>0 && i<=p2.second){
                        k-=i++;
                    }
                }
            }
            int id=vv.get(it-1).first;
            System.out.println(s.charAt(id+k+i-2));
        }
    }
}
