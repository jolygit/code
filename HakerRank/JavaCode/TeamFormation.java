import org.omg.PortableInterceptor.INACTIVE;

import java.awt.*;
import java.io.*;
import java.util.*;
class pair {
    pair(long _x,long _y){
        x=_x;
        y=_y;
    }
    public long x;
    public long  y;
}
public class Solution {
    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int t = 0; t < T; t++) {
            int N = in.nextInt();
            if (N == 0) {
                System.out.println(0);
                continue;
            }
            int[] arr = new int[N];
            int max = 0;
            for (int n = 0; n < N; n++) {
                arr[n] = in.nextInt();
                if (arr[n] > max) {
                    max = arr[n];
                }
            }
            Map<Integer,Integer> cnt = new HashMap<>();
            Arrays.sort(arr);
            for (int v : arr) {
                if(cnt.containsKey(v)){
                    cnt.put(v,cnt.get(v)+1);
                }
               else{
                    cnt.put(v,1);
                }
            }
            int minset = process(arr, cnt);
            System.out.println(minset);
        }
    }

    static int process(int[] arr, Map<Integer,Integer> cnt) {
        int minset = 100000000;
        for(int i=0;i<arr.length;i++){
            int end=i;
            while(end<arr.length && (arr[end]==arr[i] || arr[end]-arr[end-1]<=1)){
                end++;
            }
            end--;
            if(end==i){
                return 1;
            }
            else {
                minset=Math.min(minsubset(arr[i],arr[end],cnt),minset);
                i=end;
            }
        }
        return minset;
    }
    static int minsubset(int st,int end,Map<Integer,Integer> cnt) {
        ArrayList<ArrayList<Integer>> dict = new ArrayList<>();
        int min = 100000000;
        for (int i = st; i <= end; i++) {
            ArrayList<Integer> ar = new ArrayList<>();
            dict.add(ar);
        }
        for (int j = 0; j < cnt.get(st); j++) {
            dict.get(0).add(1);
        }
        for (int i = st + 1; i <=end; i++) {
            int prevCnt = dict.get(i -st- 1).size();
            int currcnt = cnt.get(i);
            ArrayList<Integer> par = dict.get(i -st- 1);
            Collections.sort(par);
            ArrayList<Integer> ar = dict.get(i-st);
            for (int c = 0; c < currcnt; c++) {
                if (c < prevCnt) {
                    ar.add(par.get(0) + 1);//double check
                    par.remove(0);
                } else {
                    ar.add(1);
                }
            }
        }
        for (int i = 0; i < dict.size(); i++) {
            ArrayList<Integer> ar = dict.get(i);
            for (int j = 0; j < ar.size(); j++) {
                min=Math.min(min,ar.get(j));
                if(min==1){
                    return 1;
                }
            }
        }
        return min;
    }
}
