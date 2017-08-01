

import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
class Edge implements Comparable<Edge>{
    Edge(int _node1,int _node2,int _wt){
        node1=_node1;
        node2=_node2;
        wt=_wt;
    }
    @Override
    public int compareTo(Edge other){
        if(wt<other.wt){
            return -1;
        }
        else if(wt>other.wt){
            return 1;
        }
        else {
            return 0;
        }
    }
    public int node1;
    public int  node2;
    public int wt;
}
public class Solution {
    static class FastReader
    {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() throws FileNotFoundException {
            // InputStream inputStream       = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(System.in));//
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
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] edges=new int[n][n];
        int Nedges=m;
        for(int e=0;e<Nedges;e++){
            int c1=in.nextInt()-1;
            int c2=in.nextInt()-1;
            int w=in.nextInt();
            if(c1!=c2 && edges[c1][c2]==0 ||  edges[c1][c2]>w) {
                edges[c1][c2] = w;
                edges[c2][c1] = w;
            }
        }
        ArrayList<Set<Integer>> trees=new ArrayList<>();
        for(int nd=0;nd<n;nd++){
            Set<Integer> tree=new HashSet<>();
            tree.add(nd);
            trees.add(tree);
        }
        ArrayList<Edge> orderedEdges=new ArrayList<>();
        for(int c1=0;c1<n;c1++) {
            for(int c2=c1+1;c2<n;c2++) {
                if(edges[c1][c2]>0) {
                    Edge ed=new Edge(c1,c2,edges[c1][c2]);
                    orderedEdges.add(ed);
                }
            }
        }
        Collections.sort(orderedEdges);
        int cntEdge=0,cntWt=0;
        for(Edge ed:orderedEdges){
            int c1=ed.node1;
            int c2=ed.node2;
            int wt=ed.wt;
            c1=findTree(c1,trees);
            Set<Integer> tree1=trees.get(c1);
            if(tree1.contains(c2)){
                continue;
            }
            else {
                c2=findTree(c2,trees);
                Set<Integer> tree2=trees.get(c2);
                tree1.addAll(tree2);
                trees.set(c2,null);
                cntWt+=wt;
                cntEdge++;
            }
            if(trees.size()==1){
                break;
            }
        }
        System.out.println(cntWt);
    }
    public static int findTree(int index,ArrayList<Set<Integer>> trees){
        int idx=trees.size()-1;
        Set<Integer> tree1=trees.get(index);
        if(tree1==null){
            while(idx>=0){
                tree1=trees.get(idx);
                if(tree1!=null && tree1.contains(index)){
                    break;
                }
                idx--;
            }
            return idx;
        }
       else{
            return index;
        }
    }
}
