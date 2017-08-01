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
                int k = in.nextInt();
                int[] arr=new int[n];
                for(int i=0;i<n;i++){
                    arr[i]=in.nextInt();
                }
                int cnt=0,min=k,last=-1,surelast=0;
                boolean good=true;

                for(int i=0;i<n;i++) {
                    if(arr[i]==1 && i<min){
                        last=i;
                    }
                    if(i>=min){
                        if(last>=0 && last<min) {
                            surelast=last;
                            cnt++;
                            min=last+2*k;
                            if(arr[i]==1){
                                last=i;
                            }
                        }
                       else{
                            good=false;
                            break;
                        }
                    }
                }
                if(surelast+k<=n-1){
                    if(last+k<=n-1){
                        good=false;
                    }
                   else {
                        cnt++;
                    }
                }
                if(good) {
                    System.out.println(cnt);
                }
                else{
                    System.out.println(-1);
                }

            }
        }

