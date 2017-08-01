import java.io.*;
import java.util.*;
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

                int n = in.nextInt();

                triplet[] A = new triplet[n];
                for(int i=0;i<n;i++){
                    int st = in.nextInt();
                    int dur = in.nextInt();
                    triplet pr=new triplet(st+dur,st,i+1);
                    A[i]=pr;
                }
                Arrays.sort(A);
                for(int i=0;i<n;i++) {
                    System.out.print(A[i].z+" ");
                }
            }
        }

