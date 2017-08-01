port java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int gemstones(String[] arr){
        ArrayList<Set<Character>> sets=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            Set<Character> set=new HashSet<>();
            String st=arr[i];
            for(int j=0;j<st.length();j++){
                Character c=st.charAt(j);
                set.add(c);
            }
            sets.add(set);
        }
        Set<Character> set1=sets.get(0);
        Set<Character> goodset=new HashSet<>();
        for(Character c:set1){
            boolean good=true;
          for(int i=1;i<sets.size();i++){
            Set<Character> set=sets.get(i);
              if(!set.contains(c)){
                  good=false;
                  break;
              }
          }
            if(good){
                goodset.add(c);
            }
        }
        return goodset.size();
        // Complete this function
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] arr = new String[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.next();
        }
        int result = gemstones(arr);
        System.out.println(result);
    }
}
