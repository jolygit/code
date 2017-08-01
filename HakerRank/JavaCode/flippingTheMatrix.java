
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
            int[][] arr = new int[2 * N][2 * N];
            for (int i = 0; i < 2 * N; i++) {
                for (int j = 0; j < 2 * N; j++) {
                    arr[i][j] = in.nextInt();
                }
            }
            int sum = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    int m1 = Math.max(arr[i][j], arr[2 * N - 1 - i][j]);
                    int m2 = Math.max(arr[i][2 * N - 1 - j], arr[2 * N - 1 - i][2 * N - 1 - j]);
                    int max = Math.max(m1, m2);
                    sum += max;
                }
            }
            System.out.println(sum);
        }

    }
}
