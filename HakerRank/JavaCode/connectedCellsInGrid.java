import java.io.*;
import java.util.*;
class pair{
   public  pair(int _i,int _j){
        i=_i;
        j=_j;
    }
   public int i;
   public int j;
}
public class Solution {
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int r = in.nextInt();
        int c = in.nextInt();
        int[][] m=new int[r][c];
        for(int i=0;i<r;i++) {
            for (int j = 0; j < c; j++) {
                m[i][j]=in.nextInt();
            }
        }
        int[][] brd=new int[r][c];
        int max=0;
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                int cnt=0;
                if(brd[i][j]==0 && m[i][j]==1) {
                    brd[i][j]=1;
                    cnt++;
                    Deque<pair> deque = new ArrayDeque<>();
                    deque.add(new pair(i, j));
                    int xn,yn;
                    while (!deque.isEmpty()) {
                        pair v = deque.removeFirst();
                        for(int x=-1;x<=1;x++){
                            for(int y=-1;y<=1;y++){
                                xn=v.i+x;
                                yn=v.j+y;
                                if(xn>=0 && xn<r && yn>=0 && yn<c && brd[xn][yn]==0 && m[xn][yn]==1){
                                    brd[xn][yn]=1;
                                    deque.add(new pair(xn, yn));
                                    cnt++;
                                }
                            }
                        }
                    }
                    if(cnt>max){
                        max=cnt;
                    }
                }
            }
        }
        System.out.println(max);
    }
}
