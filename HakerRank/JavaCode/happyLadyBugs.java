 java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int Q = in.nextInt();
        for(int a0 = 0; a0 < Q; a0++){
            int n = in.nextInt();
            String b = in.next();
            Map<Character,Integer> hash=new HashMap<Character,Integer>();
            boolean under=false;
            for(int i=0;i<n;i++){
                Character c=b.charAt(i);
                if(c=='_'){
                    under=true;
                }
                else if(hash.get(c)!=null){
                    hash.put(c,hash.get(c)+1);
                }
                else{
                    hash.put(c,1);
                }
            }
           boolean can=true;
           if(under){
            for (Integer value : hash.values()) {
              if(value==1){
                  can=false;
                  break;
              }
            }
           }
           else{
             if(n>2){
              for(int i=1;i<n-1;i++){
               if(b.charAt(i)!=b.charAt(i-1) && b.charAt(i)!=b.charAt(i+1)){
                   can=false;
                   break;
               }
              }
              if(b.charAt(0)!=b.charAt(1)){
                   can=false;
              }
              if(b.charAt(n-1)!=b.charAt(n-2)){
                   can=false;
              }  
             }
             else if(n==1){
                 can=false;
             }
             else{
              if(b.charAt(0)!=b.charAt(1)){
                   can=false;
                 
              }
            }
           }
           if(can){
                System.out.println("YES");
            }
            else{
                System.out.println("NO");
            }
        }
    }
}
