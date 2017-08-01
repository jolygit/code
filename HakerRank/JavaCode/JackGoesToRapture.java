
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
           // InputStream inputStream       = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(System.in));
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
        int N = in.nextInt();
        int M = in.nextInt();
        ArrayList<HashSet<Integer>> edges = new ArrayList<>();
        for (int n1 = 0; n1 < N; n1++) {
            HashSet<Integer> st = new HashSet<>();
            edges.add(st);
        }
        int[][] weights=new int[N][N];
        for (int a1 = 0; a1 < N ; a1++) {
            for (int a2 = 0; a2 < N ; a2++) {
                weights[a1][a2]=-1;
            }
        }
        for (int a1 = 0; a1 < M; a1++) {
            int c1 = in.nextInt() - 1;
            int c2 = in.nextInt() - 1;
            int wt = in.nextInt();
            weights[c2][c1]=wt;
            weights[c1][c2]=wt;
           // edges.get(c1).add(c2);
            //edges.get(c2).add(c1);
        }
        int end=N-1;
        int root=0;
        int dist=dijkstra(root,end,weights);
        if(dist>=0){
        System.out.println(dist);
        }
        else{
            System.out.println("NO PATH EXISTS");
        }
    }
    public static int dijkstra(int s,int end,int[][] weights){
        int N=weights[0].length;
        int[] parents=new int[N];
        long[] dist=new long[N];
        int mdist=100000000;
        for(int p=0;p<N;p++) {
            parents[p]=-1;
            dist[p]=mdist;
        }
        parents[s]=-2;//root
        dist[s]=0;
        PriorityQueue<node> pq;
        pq = new PriorityQueue<node>(N, (node a, node b) -> {
            long d=a.dist() - b.dist();
            if(d<0){
                return -1;
            }
            else if(d>0){
                return 1;
            }
            else{
                return 0;
            }
        });
        Map<Integer,node> map=new HashMap<Integer,node>();// set is used for quick find

        for(int n=0;n<N;n++) {
            node nd=new node();
            nd.index=n;
            nd.visited=false;
            if(parents[n]==-2){
                nd.dist=0;
            }
            else{
                nd.dist=mdist;
            }
            pq.add(nd);
            map.put(nd.index,nd);
        }
        for(int n=0;n<N;n++) {
            node nd=pq.poll();
            long nodeDist=(long)dist[nd.index];
            if(nd.dist==mdist){
                break;//all done
            }
            else{
                int[] neibs=weights[nd.index];
                for(int i=0;i<neibs.length;i++){
                    int neib=i;
                    if(neib==nd.index || weights[nd.index][i]==-1){
                        continue;
                    }
                    int wt=weights[nd.index][i];
                    int diff=Math.max(0,wt-(int)nodeDist);
                    if(nodeDist+diff<dist[neib]){
                        dist[neib]=nodeDist+diff;
                        node neibNode=map.get(neib);
                        pq.remove(neibNode);
                        neibNode.dist=dist[neib];
                        pq.add(neibNode);
                    }
                }
            }
        }
        if(dist[end]==mdist) {
            return -1;
        }
        else {
            return (int)dist[end];
        }
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
class node {
    public int index;
    public long dist;
    public long dist(){return dist;}
    boolean visited;
}
