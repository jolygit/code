import java.io.*;
import java.util.*;
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
class triplet implements Comparable<triplet>{
    triplet(int _x,int _y,int _z){
        x=_x;
        y=_y;
        z=_z;
    }
    @Override
    public int compareTo(triplet other){
        if(x<other.x){
            return -1;
        }
        else if(x>other.x){
            return 1;
        }
        else {
            if(z<other.z) {
                return -1;
            }
            else {
                return 1;
            }
        }
    }
    public int x;
    public int  y;
    public int z;
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

                int T = in.nextInt();
                for(int t=0;t<T;t++){
                    int X = in.nextInt();
                    X--;
                    int Y = in.nextInt();
                    Y--;
                    pair[] arr=new pair[X+Y];
                    for(int x=0;x<X;x++){
                        int val = in.nextInt();
                        pair pr=new pair(val,0);
                        arr[x]=pr;
                    }
                    for(int y=0;y<Y;y++){
                        int val = in.nextInt();
                        pair pr=new pair(val,1);
                        arr[X+y]=pr;
                    }
                    Arrays.sort(arr);
                    long cntx=1,cnty=1,cost=0,mod=1000000000+7;
                    for(int i=arr.length-1;i>=0;i--){
                        pair pr=arr[i];
                        if(pr.y==0)  {//this is x
                            cost+=cnty*(long)pr.x;
                            cost%=mod;
                            cntx++;
                        }
                        else{
                            cost+=cntx*(long)pr.x;
                            cost%=mod;
                            cnty++;
                        }
                    }
                    System.out.println(cost);
                }

            }
        }

