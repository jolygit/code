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

public class Solution {
    static class FastReader
    {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() throws FileNotFoundException {
            InputStream inputStream       = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(inputStream));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements())
            {
                try
                {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException  e)
                {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt()
        {
            return Integer.parseInt(next());
        }

        long nextLong()
        {
            return Long.parseLong(next());
        }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try
            {
                str = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return str;
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        //Scanner in = new Scanner(System.in);
        FastReader in = new FastReader();
        int q = in.nextInt();
        for (int a0 = 0; a0 < q; a0++) {
            int N = in.nextInt();
            ArrayList<HashSet<Integer>> edges = new ArrayList<>();
            for (int n1 = 0; n1 < N; n1++) {
                HashSet<Integer> st = new HashSet<>();
                edges.add(st);
            }
            for (int a1 = 0; a1 < N - 1; a1++) {
                int c1 = in.nextInt() - 1;
                int c2 = in.nextInt() - 1;
                edges.get(c1).add(c2);
                edges.get(c2).add(c1);
            }
            int g = in.nextInt();
            int k = in.nextInt();
            Map<Integer, HashSet<Integer>> guessescp = new HashMap<>();
            Map<Integer, HashSet<Integer>> guessespc = new HashMap<>();
            for (int a1 = 0; a1 < g; a1++) {
                int parent = in.nextInt() - 1;
                int child = in.nextInt() - 1;
                if (guessescp.containsKey(child)) {
                    guessescp.get(child).add(parent);
                } else {
                    HashSet<Integer> st = new HashSet<>();
                    st.add(parent);
                    guessescp.put(child, st);
                }
            }
            int[] parents = new int[N];
            int numWins = 0;
            int root = N/4;
            int cnt = 0;
            bfs(root, parents, edges);
            cnt = 0;
            int[] Cnt=new int[N];
            for (Map.Entry<Integer, HashSet<Integer>> entry : guessescp.entrySet()) {
                Integer child = entry.getKey();
                HashSet<Integer> parentset = entry.getValue();
                if (parentset.contains(parents[child])) {
                    cnt++;
                }
            }
            if (cnt >= k) {
                numWins++;
            }
            Cnt[root]=cnt;
            Set<Integer> set=new HashSet<>();
            Deque<Integer> deque=new ArrayDeque<>();
            deque.add(root);
            set.add(root);
            while(!deque.isEmpty()){
                int nd=deque.removeFirst();
                HashSet<Integer> lst=edges.get(nd);
                for(int neib:lst){
                    if( !set.contains(neib)) {
                        int parCnt=Cnt[nd];
                        deque.add(neib);
                        set.add(neib);
                        int parent=nd;
                        int child=neib;
                        if (guessescp.containsKey(child) && guessescp.get(child).contains(parent)) {
                            parCnt--;
                        }
                        if (guessescp.containsKey(parent) && guessescp.get(parent).contains(child)) {
                            parCnt++;
                        }
                        if (parCnt >= k) {
                            numWins++;
                        }
                        Cnt[neib]=parCnt;
                    }
                }
            }
            int gc = gcd(N, numWins);
            N /= gc;
            numWins /= gc;
            System.out.println(numWins + "/" + N);
        }
    }
    static int gcd(int a, int b)
    {
        if(a == 0 || b == 0) return a+b; // base case
        return gcd(b,a%b);
    }
    private static void bfs(int root,int[] parents,ArrayList<HashSet<Integer>> edges){
        Set<Integer> set=new HashSet<>();
        Deque<Integer> deque=new ArrayDeque<>();
        deque.add(root);
        set.add(root);
        parents[root]=-1;
        while(!deque.isEmpty()){
            int nd=deque.removeFirst();
            HashSet<Integer> lst=edges.get(nd);
            for(int neib:lst){
                if( !set.contains(neib)) {
                    deque.add(neib);
                    set.add(neib);
                    parents[neib]=nd;
                }
            }
        }
    }
}
