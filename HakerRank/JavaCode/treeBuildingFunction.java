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
//asumming there are N nodes and M edges
int N = in.nextInt();
ArrayList<HashSet<Integer>> edges = new ArrayList<>();
for (int n1 = 0; n1 < N; n1++) {
    HashSet<Integer> st = new HashSet<>();
    edges.add(st);
}
for (int a1 = 0; a1 < M; a1++) {
    int c1 = in.nextInt() - 1;
    int c2 = in.nextInt() - 1;
    edges.get(c1).add(c2);
    edges.get(c2).add(c1);
}
int[] parents = new int[N];
int root=0;
bfs(root, parents, edges);//parents is the array of parents of a given node.
