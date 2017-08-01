import javax.print.DocFlavor;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    static boolean check(Map<Character,Integer> map,int N){
        if((map.get('A')!=null && map.get('A')>N)|| (map.get('C')!=null && map.get('C')>N) || (map.get('G')!=null && map.get('G')>N)|| (map.get('T')!=null && map.get('T')>N)){
            return false;
        }
        return true;
    }
    static int sherlockAndAnagrams(String s){
        char[] arr=s.toCharArray();
        int l=arr.length;
        Map<Character,Integer> map=new HashMap<>();
        int max,min=0,subsize=0;
        for(max=l-1;max>=0;max--){
            Character c=arr[max];
            map.put(c,map.get(c)==null?1:(map.get(c)+1));
            if(!check(map,l/4)){
                map.put(c,map.get(c)-1);
                max++;
                break;
            }
        }
        if(max==-1){
            return 0;
        }
        subsize=max;
        for(min=0;min<max;min++){
            Character c=arr[min];
            map.put(c,map.get(c)==null?1:(map.get(c)+1));
            while(!check(map,l/4) && max<l-1){
                Character cc=arr[max];
                map.put(cc,map.get(cc)-1);
                max++;
            }
            if(subsize>(max-min-1)){
                subsize=max-min-1;
            }
            if(max==l-1){
                break;
            }
        }
        return subsize;
    }

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int N=in.nextInt();
        String s = in.next();
        int result = sherlockAndAnagrams(s);
        System.out.println(result);
    }
}
