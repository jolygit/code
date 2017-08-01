import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Scanner in = new Scanner(System.in);
        int Q = in.nextInt();
	//   int[] ar = new int[n];
        for(int q=0;q<Q;q++){
            int Nnodes=in.nextInt();
            int[] parents = new int[Nnodes];
            for(Integer p:parents){
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
            int depth=0;
            int[] depths = new int[Nnodes];
            for(int d=0;d<depths.length;d++){
                depths[d]=-1;
            }
            depths[root]=-2;
            Deque<Integer> deque = new ArrayDeque<Integer>();
            Set<Integer> set = new HashSet<Integer>();
            set.add(root);
            deque.add(root);
            int[] parent = new int[Nnodes];
            for(int d=0;d<parent.length;d++){
                parent[d]=-1;
            }
            parent[root]=-2;
            while(!deque.isEmpty()){
                int nd=deque.removeFirst();
                // if(!set.contains(nd)){
		List<Integer> l1=list.get(nd);
		for(int chnode:l1){
		    if(!set.contains(chnode)) {
			deque.add(chnode);
			set.add(chnode);
			parent[chnode]=nd;
		    }
		}
                //}
            }
            for(int p=0;p<parent.length;p++){
                depth=0;
                if(parent[p]==-1) {
                    System.out.print(-1+" ");
                }
                else{
                    int qq=p;
                    while(parent[qq]!=-2){
                        depth++;
			qq=parent[qq];
                    }
                    System.out.print(depth*6+" ");
                }

            }
            System.out.println();
        }
    }
}
