import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() throws FileNotFoundException {
            InputStream inputStream = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(inputStream));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //Scanner in = new Scanner(System.in);
        FastReader in = new FastReader();
        int Q = in.nextInt();
        for (int q = 0; q < Q; q++) {
            int N = in.nextInt();
            pair[] arr = new pair[N];
            for (int i = 0; i <  N; i++) {
                int v = in.nextInt();
                pair pr=new pair(v,i);
                arr[i]=pr;
            }
            Arrays.sort(arr);
            for(int i=N-2;i>0;i--){
                if(arr[i].x==arr[i+1].x){
                    int minidx=Math.min(arr[i].y,arr[i+1].y);
                    arr[i].y=minidx;
                    arr[i+1]=null;
                }
            }
            int cnt=0,idx=N-1;
            int maxArrIdx=N-1;
            while(true){
                if(arr[idx]==null){
                    idx--;
                    continue;
                }
                int ArrIdx=arr[idx].y;
                if(ArrIdx<=maxArrIdx){
                    maxArrIdx=ArrIdx;
                    cnt++;
                    if(maxArrIdx==0){
                        break;
                    }
                }
                idx--;
            }
            if(cnt%2==0){
                System.out.println("ANDY");
            }
            else {
                System.out.println("BOB");
            }
        }

    }
}
class pair implements Comparable<pair>{
    pair(int _x,int _y){
        x=_x;
        y=_y;
    }
    @Override
    public int compareTo(pair other){
        if(x<other.x){
            return -1;
        }
        else if(x>other.x){
            return 1;
        }
        else {
            return 0;
        }
    }
    public int x;
    public int  y;
}
