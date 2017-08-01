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
            int[] orarr=new int[N];
            for (int i = 0; i <  N; i++) {
                int v = in.nextInt();
                pair pr=new pair(v,i);
                arr[i]=pr;
                orarr[i]=v;
            }
            Arrays.sort(arr);
            int cnt=0;
            boolean toochaot=false;
            for (int i = N-1; i >0; i--) {
               int max=arr[i].x;
               int maxidx=arr[i].y;
               if(i-maxidx>2){
                   System.out.println("Too chaotic");
                   toochaot=true;
                   break;
               }
               else if(i==maxidx){
                   continue;
               }
               else {
                   int dif=i-maxidx;
                   if(dif==1){
                       int v1=orarr[maxidx+1];
                       orarr[maxidx]=orarr[maxidx+1];
                       int tmpidx=i;
                       while(true){
                           if(arr[tmpidx].x==v1){
                               arr[tmpidx].y=arr[tmpidx].y-1;
                               cnt++;
                               break;
                           }
                           tmpidx--;
                       }
                   }
                   else{
                       int v1=orarr[maxidx+1];
                       int v2=orarr[maxidx+2];
                       orarr[maxidx]=orarr[maxidx+1];
                       orarr[maxidx+1]=orarr[maxidx+2];
                       int tmpidx=i;
                       int cnter=0;
                       while(true){
                           if(arr[tmpidx].x==v1 || arr[tmpidx].x==v2){
                               arr[tmpidx].y=arr[tmpidx].y-1;
                               cnter++;
                               cnt++;
                               if(cnter==2) {
                                   break;
                               }
                           }
                           tmpidx--;
                       }
                   }
               }
            }
            if(!toochaot) {
                System.out.println(cnt);
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
