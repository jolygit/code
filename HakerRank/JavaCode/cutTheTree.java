
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
class gnode{
    public   gnode(){edges=new ArrayList<>();};
    public ArrayList<Integer> edges;
    int index;
}
class tnode{
    public   tnode(){parent=null;sum=0;children=new ArrayList<>();};
    public   Integer parent;
    int data;
    public ArrayList<Integer> children;
    public int sum;
}
public class Solution {

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int N = in.nextInt();
        int[] data=new int[N];
        for(int n=0;n<N;n++){
            int d= in.nextInt();
            data[n]=d;
        }
        ArrayList<gnode> graph=new ArrayList<>();
        ArrayList<tnode> tree=new ArrayList<>();
        for(int n=0;n<N;n++){
            gnode nd=new gnode();
            nd.index=n;
            graph.add(nd);
            tnode tnd=new tnode();
            tree.add(tnd);
        }
        for(int n=0;n<N-1;n++){
            int u= in.nextInt()-1;
            int v= in.nextInt()-1;
            graph.get(u).edges.add(v);
            graph.get(v).edges.add(u);
        }
        createTree(graph,tree);
        ComputeSum(tree,tree.get(0),data,0);
        int sumroot=tree.get(0).sum;
        int min=1000000;
        for(int n=1;n<N;n++){
            tnode nd=tree.get(n);
            int dif=Math.abs(sumroot-2*nd.sum);
            if(min>dif){
                min=dif;
            }
        }
        System.out.println(min);
    }
    public static void ComputeSum(ArrayList<tnode> tree,tnode root,int[] data,int rtIndex){
        for(Integer chd:root.children){
            tnode tchd=tree.get(chd);
            ComputeSum(tree,tchd,data,chd);
            root.sum+=tchd.sum;
        }
        root.sum+=data[rtIndex];
    }
    public static void createTree(ArrayList<gnode> graph,ArrayList<tnode> tree){
        gnode gfrst=graph.get(0);
        tnode root=tree.get(gfrst.index);
        root.parent=null;
        Set<Integer> visited=new HashSet<>();
        visited.add(gfrst.index);
        Deque<gnode> deque=new ArrayDeque<>();
        deque.add(gfrst);
        while (!deque.isEmpty()){
            gnode nd=deque.removeFirst();
            for(Integer chidx:nd.edges){
                if(!visited.contains(chidx)){
                    visited.add(chidx);
                    gnode gchld=graph.get(chidx);
                    deque.add(gchld);
                    tnode parent=tree.get(nd.index);
                    tnode tchld=tree.get(gchld.index);
                    parent.children.add(gchld.index);
                    tchld.parent=nd.index;
                }
            }
        }
    }

}
