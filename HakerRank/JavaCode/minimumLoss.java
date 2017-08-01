import java.io.*;
import java.util.*;
class pair implements Comparable<pair>{
  public  pair(int _i,long _v){
        i=_i;
        v=_v;
    }
    public int i;
    public long v;
    @Override
    public int compareTo(pair other) {
        if(this.v - other.v>0){
            return 1;
        }
        else if(this.v - other.v<0){
            return -1;
        }
        else{
            return 0;
        }
    }
}
public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        pair[] ar=new pair[n];
        
        for(int i=0;i<n;i++){
            long v = in.nextLong();
            pair pr=new pair(i,v);
            ar[i]=pr;
        }
        Arrays.sort(ar);
        long min=1000000000;
        for(int i=0;i<n-1;i++){
            if(ar[i+1].v-ar[i].v<min && ar[i+1].i<ar[i].i){
                min=ar[i+1].v-ar[i].v;
            }
        }
        System.out.print(min);
    }
}
