
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
class node {
    public int index;
    public long dist;
    public long dist(){return dist;}
    boolean visited;
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
       /* Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        //Scanner in = new Scanner(System.in);
        FastReader in=new FastReader();
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int N = in.nextInt();
            int m = in.nextInt();
            int[][] weights=new int[N][N];
            for(int a1 = 0; a1 < m; a1++){
                int x = in.nextInt()-1;
                int y = in.nextInt()-1;
                int w = in.nextInt();
                if(weights[x][y]==0 || weights[x][y]>w){
                    weights[x][y]=w;
                    weights[y][x]=w;
                }
            }
            int s = in.nextInt()-1;
            int[] parents=new int[N];
            long[] dist=new long[N];
            int mdist=1000000;
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
                //map.remove(nd.index);
                long nodeDist=(long)dist[nd.index];
                if(nd.dist==mdist){
                    break;//all done
                }
                else{
                    int[] neibs=weights[nd.index];
                    for(int i=0;i<neibs.length;i++){
                        int neib=i;
                        if(neib==nd.index || weights[nd.index][i]==0){
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
            for(int el=0;el<N;el++) {
                if(dist[el]!=0 && dist[el]!=mdist) {
                    System.out.print(dist[el]+" ");
                }
                if(dist[el]==mdist){
                    System.out.print(-1+" ");
                }
            }
            System.out.println();
        }
    }
}
