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
//List representation
class pair{
    pair(int _idx,int _wt){
        wt=_wt;
        idx=_idx;
    }
    int wt;
    int idx;
}
//this is how to fill the weights
ArrayList<ArrayList<pair>> edges = new ArrayList<>();
for (int n1 = 0; n1 < N; n1++) {
    ArrayList<pair> st = new ArrayList<>();
    edges.add(st);
}
for (int a1 = 0; a1 < M; a1++) {
    int c1 = in.nextInt() - 1;
    int c2 = in.nextInt() - 1;
    int wt = in.nextInt();
    pair pr1=new pair(c1,wt);
    pair pr2=new pair(c2,wt);
    edges.get(c1).add(pr2);
    edges.get(c2).add(pr1);
}
///////////////////////////
public static int dijkstra(int s,int end,ArrayList<ArrayList<pair>>  weights){
        int N=weights.size();
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
                ArrayList<pair> neibs=weights.get(nd.index);
                for(int i=0;i<neibs.size();i++){
                    int neib=neibs.get(i).idx;
                    int wt=weights.get(nd.index).get(i).wt;
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
class node {
    public int index;
    public long dist;
    public long dist(){return dist;}
    boolean visited;
}

