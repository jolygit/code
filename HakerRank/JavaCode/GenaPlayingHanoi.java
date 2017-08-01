
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
        int[] initPosArr=new int[N];
        int initPos=0;
        for(int n=0;n<N;n++){
            int rodNum= in.nextInt()-1;
            initPosArr[N-1-n]=rodNum;

        }
        for(int n=N-1;n>=0;n--){
            initPos+=initPosArr[n];
            initPos<<=2;
        }
        initPos>>=2;
        int S=1;
        S<<=2*N;
        int[][] states=new int[S][6];
        for(int s=0;s<S;s++){
            for(int i=0;i<6;i++) {
                states[s][i] = -1;
            }
        }
        int[] top=new int[4];
        int posid=0;
        for(int s=0;s<S;s++){
            posid=s;
            getTopDisks(posid,N,top);
            int connection=0;
            for(int rd=0;rd<4;rd++){
                for(int ord=0;ord<4;ord++){
                    if(rd!=ord && top[rd]>top[ord]){
                        int cnt=top[rd];
                        int left=posid;
                        left>>=2*(cnt+1);
                        left<<=2*(cnt+1);
                        int right=1;
                        right<<=2*cnt;
                        right--;
                        right=right&posid;
                        int center=ord;
                        center<<=2*cnt;
                        int newposid=left+center+right;
                        states[posid][connection]=newposid;
                        connection++;
                    }
                }
            }
        }
        int L=findPathLength(states,initPos,S);
        System.out.println(L);//posid+" "+top[0]+" "+top[1]+" "+top[2]+" "+top[3]);
    }
    public static int findPathLength(int[][] states,int initPos,int N){
        Deque<Integer> deque=new ArrayDeque<>();
        deque.add(initPos);
        boolean[] visited=new boolean[N];
        for(int i=0;i<N;i++){
            visited[i]=false;
        }
        visited[initPos]=true;
        int[] parents=new int[N];
        parents[initPos]=-1;
        boolean found=false;
        while(!deque.isEmpty() && !found){
            int val=deque.removeFirst();
            for(int i=0;i<6;i++) {
                int chld=states[val][i];
                if (chld != -1 && !visited[chld]) {
                  deque.add(chld);
                  visited[chld]=true;
                  parents[chld]=val;
                  if(chld==0){
                      found=true;
                      break;
                  }
                }
            }
        }
        int L=0,ch=0;
        while(parents[ch]!=-1){
            L++;
            ch=parents[ch];
        }
        return L;
    }
    public static int getRod(int Position,int diskNum){
        int three=3;
        Position>>=2*diskNum;
        return Position&three;
    }
    public static void getTopDisks(int Position,int N,int[] top){
        int three=3;
        top[0]=-1;
        top[1]=-1;
        top[2]=-1;
        top[3]=-1;
        for(int i=0;i<N;i++){
            int indx=Position&three;
            if(i>top[indx]){
                top[Position&three]=i;
            }
            Position>>=2;
        }
    }
 }
