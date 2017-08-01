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

                int n = in.nextInt();
                int[] arr=new int[n];
                for(int i=0;i<n;i++){
                    arr[i]=in.nextInt();
                }
                int x=0,y=0,b=0;
                while(n>0){
                    b=arr[n-1];
                    y=(x+b)/2;
                    if((x+b)%2==1){
                        y++;
                    }
                    x=y;
                    n--;
                }
                System.out.println(x);
            }
        }

