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
           // InputStream inputStream = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(System.in));
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
        int a=5;
        int b=8;
        int l=a^a^a^a^b^b;
        int[][][] f=new int[13][101][101];
        int[][][] ss=new int[13][101][101];

        //freopen("c:/Users/alex/code/HackerRank/JavaCode/input.txt", "r", stdin);
        FastReader in = new FastReader();
        int n=in.nextInt();
        int q=in.nextInt();
        int c=in.nextInt();
        c++;
        for(int i=0;i<11;i++){
            for(int j=0;j<101;j++){
                for(int k=0;k<101;k++){
                    f[i][j][k]=0;
                    ss[i][j][k]=0;
                }
            }
        }
        for(int i=0;i<n;i++){
            int x=in.nextInt();
            int y=in.nextInt();
            int s=in.nextInt();
            for(int v=0;v<c;v++){
                f[v][x][y]+=s;
                s++;
                if(s==c)
                    s=0;
            }
        }
        for(int i=0;i<11;i++){
            for(int j=1;j<101;j++){
                for(int k=1;k<101;k++){
                    ss[i][j][k]+=f[i][j][k]+ss[i][j][k-1]+ss[i][j-1][k]-ss[i][j-1][k-1];
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<q;i++){
            int t=in.nextInt();
            int X1=in.nextInt();
            int Y1=in.nextInt();
            int X2=in.nextInt();
            int Y2=in.nextInt();
            int TB=0;
            t%=c;
            TB+=ss[t][X2][Y2]+ss[t][X1-1][Y1-1]-ss[t][X1-1][Y2]-ss[t][X2][Y1-1];
            sb.append(TB).append("\n");
        }
        
    }

}
