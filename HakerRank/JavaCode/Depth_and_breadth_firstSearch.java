import java.io.*;
import java.util.*;

class Solution {
    public static void main(String args[]) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int Nnodes=in.nextInt();
        int[] parents = new int[Nnodes];
        for(int p=0;p<Nnodes;p++){
            parents[p]=-1;
        }
        List< List<Integer> > list = new ArrayList<>();
        for(int i=0;i<Nnodes;i++){
            List<Integer> lst=new ArrayList<>();
            list.add(lst);
        }
        int Nedges=in.nextInt();
        for(int e=0;e<Nedges;e++){
            int n1=in.nextInt()-1;
            int n2=in.nextInt()-1;
            List<Integer> l1=list.get(n1);
            l1.add(n2);
            List<Integer> l2=list.get(n2);
            l2.add(n1);
        }
        int root=in.nextInt()-1;
        dfs(list,root,parents);
        print(parents);
    }
    private static void print(int[] parents){
        for(int n=0;n<parents.length;n++){
            int par=parents[n];
            if(par!=-2 && par!=-1) {
                while (par>0) {
                   par=parents[par];
                }
                System.out.print(n+1+" ");
            }
            if(parents[n]==-2){
                System.out.print(n+1+" ");
            }
        }
    }
   private static void bfs(List<List<Integer>> list,int root,int[] parents){
       Set<Integer> set=new HashSet<>();
       Deque<Integer> deque=new ArrayDeque<>();
       deque.add(root);
       set.add(root);
       parents[root]=-2;
       while(!deque.isEmpty()){
           int nd=deque.removeFirst();
           List<Integer> lst=list.get(nd);
           for(int neib:lst){
               if(!set.contains(neib)) {
                   deque.add(neib);
                   set.add(neib);
                   parents[neib]=nd;
               }
           }
       }
    }
    private static void dfs(List<List<Integer>> list,int root,int[] parents){
        Set<Integer> set=new HashSet<>();
        Stack<Integer> stack=new Stack<>();
        stack.add(root);
        set.add(root);
        parents[root]=-2;
        while(!stack.isEmpty()){
            int nd=stack.pop();
            List<Integer> lst=list.get(nd);
            for(int neib:lst){
                if(!set.contains(neib)) {
                    stack.add(neib);
                    set.add(neib);
                    parents[neib]=nd;
                }
            }
        }
    }
}
