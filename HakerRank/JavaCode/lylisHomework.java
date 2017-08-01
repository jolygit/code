import java.io.*;
import java.util.*;

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
        int[] arr=new int[n];
        int[] arrRev=new int[n];
        int[] arrs=new int[n];
        Map<Integer,Integer> map=new HashMap<>();
        Map<Integer,Integer> mapRev=new HashMap<>();
        for(int i=0;i<n;i++){
            int v=in.nextInt();
            arr[i]=v;
            arrs[i]=v;
            map.put(v,i);
        }
        for(int i=0;i<n;i++) {
            int v=arr[i];
            int idx=arr.length-1-i;
            arrRev[idx]=v;
            mapRev.put(v,idx);
        }
        int cnt=0,cntr=0;
        Arrays.sort(arrs);
        for(int i=0;i<arrs.length;i++){
            int v=arrs[i];
            if(arr[i]!=arrs[i]){
                cnt++;
                int idx=map.get(v);
                arr[idx]=arr[i];
                arr[i]=arrs[i];
                map.put(arr[idx],idx);
            }
            if(arrRev[i]!=arrs[i]){
                cntr++;
                int idx=mapRev.get(v);
                arrRev[idx]=arrRev[i];
                arrRev[i]=arrs[i];
                mapRev.put(arrRev[idx],idx);
            }
        }
        System.out.print(Math.min(cnt,cntr));
    }
}
