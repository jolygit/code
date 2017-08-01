
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
    public   tnode(){
        parent=null;children=new ArrayList<>();
        sum=new long[2][2];
        sum[0][0]=-1;
        sum[0][1]=-1;
        sum[1][0]=-1;
        sum[1][1]=-1;
    };
    public   Integer parent;
    int data;
    public ArrayList<Integer> children;
    public long[][] sum;
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
        long sum=0;
        sum+=Compute(tree,tree.get(0),0,-1);
        sum+=Compute(tree,tree.get(0),1,-1);
        int modulo=1000000000+7;
        System.out.println(sum%modulo);
    }
    public static long Compute(ArrayList<tnode> tree,tnode root,int colorSelf,int colorParent){//0 red 1 black
        long prod=1;
        long modulo=1000000000+7;
        if((colorParent>=0  && root.sum[colorSelf][colorParent]==-1) || (colorParent==-1)) {
            ArrayList<Integer> children = root.children;
            if(children.size()>0) {
                for (Integer ch : children) {
                    long tmp= (Compute(tree, tree.get(ch), 0, colorSelf) + Compute(tree, tree.get(ch), 1, colorSelf));
                    tmp%=modulo;
                    prod%=modulo;
                    prod*=tmp;
                    prod%=modulo;
                }
                if (colorParent != colorSelf) {
                    int prod2 = 1;
                    if (colorParent == -1) {
                        colorParent = Math.abs(colorSelf - 1);
                    }
                    for (Integer ch : children) {
                        long tmp= (Compute(tree, tree.get(ch), colorParent, colorSelf));
                        tmp%=modulo;
                        prod2%=modulo;
                        prod2*=tmp;
                        prod2%=modulo;
                    }
                    prod -= prod2;
                }
                prod%=modulo;
                root.sum[colorSelf][colorParent]=prod;
            }
            else {
                root.sum[0][0]=1;
                root.sum[0][1]=0;
                root.sum[1][0]=0;
                root.sum[1][1]=1;
                prod=root.sum[colorSelf][colorParent];
            }
        }
        else{
            prod=root.sum[colorSelf][colorParent];
        }
        return prod;
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
