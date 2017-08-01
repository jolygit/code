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
    public static void main(String[] args) throws FileNotFoundException{
        //Scanner in = new Scanner(System.in);
        FastReader in=new FastReader();
        int T = in.nextInt();
        for(int t=0;t<T;t++) {
            int L = in.nextInt();
            Map<Integer,Integer> jumps=new HashMap<>();
            for(int l=0;l<L;l++){
                int st = in.nextInt()-1;
                int end = in.nextInt()-1;
                jumps.put(st,end);
            }
            int S = in.nextInt();
            for(int l=0;l<S;l++){
                int st = in.nextInt()-1;
                int end = in.nextInt()-1;
                jumps.put(st,end);
            }
            int N=100;
            int[][] edges = new int[N][N];
            for (int c1 = 0; c1 < N; c1++) {
                for (int c2 = 0; c2 < N; c2++) {
                    edges[c1][c2] = -1;
                }
            }
            for (int c1 = 0; c1 < N; c1++) {
                if(jumps.containsKey(c1)){
                    int c2=jumps.get(c1);
                    edges[c1][c2]=0;
                }
                else{
                    for(int r=1;r<=6;r++){
                        int next=c1+r;
                        if(next<=99){
                            edges[c1][next]=1;
                        }
                    }
                }
            }
            int start=0;
            int end=99;
            int dist=dijkstra(start,end,edges);
            System.out.println(dist);
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
                    if(nodeDist+wt<dist[neib]){
                        dist[neib]=nodeDist+(long)wt;
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
}
class node {
    public int index;
    public long dist;
    public long dist(){return dist;}
    boolean visited;
}
