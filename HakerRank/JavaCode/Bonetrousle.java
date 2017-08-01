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
            long n = in.nextLong();
            long k = in.nextLong();
            long b = in.nextLong();
            long numer=(n-b*(b-1)/2);
            long st=numer/b;
            long suml=st*b+b*(b-1)/2;
            long dfl=n-suml+(st+b-1);
            long sumh=(st+1)*(b)+b*(b-1)/2;
            long difh=sumh-n;
            long frac=st%b;
            if (numer<0 || st+b-1>k || st==0){
                System.out.println(-1);
            }
            else if(suml==n){
                for(long i=st;i<st+b;i++){
                    System.out.print(i+" ");
                }
                System.out.println();
            }
            else if(dfl<=k){
                long sm=0;
                for(long i=st;i<st+b-1;i++){
                    sm+=i;
                    System.out.print(i+" ");
                }
                if(n-sm>k){
                    System.out.println(-1);
                }
                else {
                    System.out.print(n - sm);
                    System.out.println();
                }
            }
            else {
                long cnt=difh;
                if((st==0 && cnt>0)|| st+b>k){
                    System.out.println(-1);
                }
                else {
                    for (long i = st + 1; i < st + b + 1; i++) {
                        if (cnt > 0) {
                            System.out.print(i - 1 + " ");
                        } else {
                            System.out.print(i + " ");
                        }
                        cnt--;
                    }
                    System.out.println();
                }
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
