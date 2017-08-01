import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long[] l = new long[n];
        for(int i=0;i<n;i++){
            int v = in.nextInt();
            l[i]=v;
        }
    Arrays.sort(l);
        boolean found=false;
        for(int i=n-1;i>=2;--i){
            if(l[i-2]+l[i-1]>l[i]){
                System.out.println(l[i-2]+" "+l[i-1]+" "+l[i]);
                found=true;
                break;
            }
        }
        if(!found){
          System.out.println(-1);
        }
    }
}
