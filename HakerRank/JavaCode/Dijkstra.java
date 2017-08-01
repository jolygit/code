/*one test does not pass cos it is slow need to use fibonachi heaps
 */

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
    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int N = in.nextInt();
            int m = in.nextInt();
            ArrayList<ArrayList<Integer>> edges=new ArrayList<>();
            ArrayList<ArrayList<Integer>> weights=new ArrayList<>();
            for(int n=0;n<N;n++) {
                ArrayList<Integer> e=new ArrayList<Integer>();
                edges.add(e);
                ArrayList<Integer> w=new ArrayList<Integer>();
                weights.add(w);
            }
            for(int a1 = 0; a1 < m; a1++){
                int x = in.nextInt()-1;
                int y = in.nextInt()-1;
                int w = in.nextInt();
                boolean found=false;
                for(int i=0;i<edges.get(x).size();i++){
                    if(edges.get(x).get(i)==y && weights.get(x).get(i)>w){
                        weights.get(x).set(i,w);
                        found=true;
                        break;
                    }
                }
                if(found){
                    for(int i=0;i<edges.get(y).size();i++){
                        if(edges.get(y).get(i)==x && weights.get(y).get(i)>w){
                            weights.get(y).set(i,w);
                            break;
                        }
                    }
                }
                else{
                    edges.get(x).add(y);
                    edges.get(y).add(x);
                    weights.get(x).add(w);
                    weights.get(y).add(w);
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
                    ArrayList<Integer> nodeEdges=edges.get(nd.index);
                    ArrayList<Integer> nodeEdgeWeights=weights.get(nd.index);
                    for(int i=0;i<nodeEdges.size();i++){
                        int neib=nodeEdges.get(i);
                        int wt=nodeEdgeWeights.get(i);
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


