/*
Watson gives Sherlock an array A1,A2...AN. 
He asks him to find an integer M between P and Q(both inclusive), such that, min {|Ai-M|, 1 �� i �� N} is maximised. If there are multiple solutions, print the smallest one.

Input Format

The first line contains N. The next line contains space separated N integers, and denote the array A. The third line contains two space separated integers denoting P and Q.
 */
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
                int p = in.nextInt();
                int q = in.nextInt();
                Arrays.sort(arr);
                int M=0,maxd=0;
                for(int i=1;i<n;i++){
                    int v=arr[i];
                    int u=arr[i-1];
                    if(v<p || u>q){
                        continue;
                    }
                    if(u<p && v>q){// u ... p...q....v
                        int halfway=(v-u)/2;
                        if(halfway<p){
                            M=p;
                        }
                        else if(halfway>q){
                            M=q;
                        }
                        else {
                            M=halfway;
                        }
                    }
                    if(u<q){// p ... u...q....v
                        int halfway=Math.min(q-u,(v-u)/2);
                      if(i==1){
                          if(u-p>=halfway){
                              M=p;
                          }
                          else {
                              M=u+halfway;
                          }
                      }
                      else {
                          int lowerhalfway=Math.min(u-p,(u-arr[i-2])/2);
                          if(lowerhalfway>=halfway && lowerhalfway>maxd){
                              M=u-lowerhalfway;
                              maxd=lowerhalfway;
                          }
                          else if(halfway>maxd){
                              M=u+halfway;
                              maxd=halfway;
                          }
                      }
                        continue;
                    }
                    else if(v<q && p>u){// u ... p...v....q
                       int halfway=Math.min((v-u)/2,v-p);
                       if(halfway>maxd) {
                           M = v - halfway;
                           maxd = halfway;
                       }
                        continue;
                    }
                    int d=v-u;
                    if(d>maxd){
                        M=u+d/2;
                        maxd=d/2;
                    }
                }
                if(p<arr[0] && arr[0]-p>=maxd){
                    M=p;
                }
                if(q>arr[n-1] && q-arr[n-1]>=maxd){
                    M=q;
                }
                System.out.println(M);
            }
        }

