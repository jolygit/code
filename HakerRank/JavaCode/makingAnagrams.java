import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int makingAnagrams(String s1, String s2){
        HashMap<Character,Integer> h1=new HashMap<Character,Integer>();
        for(int i=0;i<s1.length();i++){
            Character c=s1.charAt(i);
            if(h1.containsKey(c)){
                Integer val=h1.get(c);
                h1.put(c,val+1);
            }
            else{
               h1.put(c,1); 
            }
        }
        HashMap<Character,Integer> h2=new HashMap<Character,Integer>();
        for(int i=0;i<s2.length();i++){
            Character c=s2.charAt(i);
            if(h2.containsKey(c)){
                Integer val=h2.get(c);
                h2.put(c,val+1);
            }
            else{
               h2.put(c,1); 
            }
        }
        int cnt=0;
        Iterator it1 = h1.entrySet().iterator();
        while (it1.hasNext()) {
         Map.Entry pair1 = (Map.Entry)it1.next();
         Character c1=(Character)pair1.getKey() ;
         Integer   v1=(Integer)pair1.getValue();
            if(!h2.containsKey(c1)){
                cnt+=v1;
            }
            else{
                cnt+=Math.abs(h1.get(c1)-h2.get(c1));
            }
        }
        // Complete this function
        Iterator it2 = h2.entrySet().iterator();
        while (it2.hasNext()) {
         Map.Entry pair2 = (Map.Entry)it2.next();
         Character c2=(Character)pair2.getKey() ;
         Integer   v2=(Integer)pair2.getValue();
            if(!h1.containsKey(c2)){
                cnt+=v2;
            }
        }
        return cnt;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s1 = in.next();
        String s2 = in.next();
        int result = makingAnagrams(s1, s2);
        System.out.println(result);
    }
}
