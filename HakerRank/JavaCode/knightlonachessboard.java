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
        int n = in.nextInt();
        int[][] brd=new int[n][n];
        pair[][] brdp=new pair[n][n];
        pair zero=new pair(-1,-1);
        brdp[0][0]=zero;
        for(int i=1;i<n;i++){
            for(int j=1;j<n;j++){
                for(int i1=0;i1<n;i1++){
                    for(int j1=0;j1<n;j1++) {
                        pair z=new pair(-1,-1);
                        brdp[i1][j1]=z;
                        brd[i1][j1]=0;
                    }
                }
                brd[0][0]=1;
                Deque<pair> deque=new ArrayDeque<>();
                deque.add(new pair(0,0));
                int x2,x22,y2,y22,x0=0,y0=0;
                while(!deque.isEmpty()) {
                    pair v=deque.removeFirst();
                    x0=v.i;
                    y0=v.j;
                    for (int sx = 0; sx < 2; sx++) {
                        for (int sy = 0; sy < 2; sy++) {
                            x2 = sx==0?i:-i;
                            x2+=x0;
                            y2 = sy==0?j:-j;
                            y2+=y0;
                            if (x2 >= 0 && x2 < n && y2 >= 0 && y2 < n && brd[x2][y2] == 0) {
                                brd[x2][y2] = 1;
                                deque.add(new pair(x2,y2));
                                brdp[x2][y2].i=x0;
                                brdp[x2][y2].j=y0;
                            }
                            x22 = sx==0?j:-j;
                            x22+=x0;
                            y22 = sy==0?i:-i;
                            y22+=y0;
                            if (x22 >= 0 && x22 < n && y22 >= 0 && y22 < n && brd[x22][y22] == 0) {
                                brd[x22][y22] = 1;
                                deque.add(new pair(x22,y22));
                                brdp[x22][y22].i=x0;
                                brdp[x22][y22].j=y0;
                            }
                        }
                    }
                }
                int cnt=0,sx=n-1,sy=n-1;
                while(sx>=0 && sy>=0 && brdp[sx][sy].i!=-1 && brdp[sx][sy].j!=-1){
                    int tsx=brdp[sx][sy].i;
                    int tsy=brdp[sx][sy].j;
                    sx=tsx;
                    sy=tsy;
                    cnt++;
                }
                if(cnt==0){
                    cnt=-1;
                }
                System.out.print(cnt+" ");
            }
            System.out.println();
        }
    }
}
