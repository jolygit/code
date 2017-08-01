/*
Given a grid of size , each cell in the grid is either  or .

A valid plus is defined here as the crossing of two segments (horizontal and vertical) of equal lengths. These lengths must be odd, and the middle cell of its horizontal segment must cross the middle cell of its vertical segment.

In the diagram below, the blue pluses are valid and the orange ones are not valid. pluseses.png

Find the  valid pluses that can be drawn on  cells in the grid, and print an integer denoting the maximum product of their areas.

Note: The two pluses cannot overlap, and the product of their areas should be maximal.

Input Format

The first line contains two space-separated integers,  and .
The  subsequent lines contains  characters, where each character is either G () or B (). If the character in the  line is G, then  is a  cell (otherwise it's a  cell).

Constraints



Output Format

Find  pluses that can be drawn on  cells of the grid, and print an integer denoting the maximum product of their areas.

Sample Input 0

5 6
GGGGGG
GBBBGB
GGGGGG
GGBBGB
GGGGGG
Sample Output 0

5
Sample Input 1

6 6
BGBBGB
GGGGGG
BGBBGB
GGGGGG
BGBBGB
BGBBGB
Sample Output 1

25
Explanation

Here are two possible solutions for Sample 1 (left) and Sample 2 (right): plusss.png

Explanation Key:

Green:  cell
Red:  cell
Blue: possible .
For the explanation below, we will refer to a plus of length  as .

Sample 0 
There is enough good space to color one  plus and one  plus. , and . The product of their areas is , so we print .

Sample 1 
There is enough good space to color two  pluses. . The product of the areas of our two pluses is , so we print .
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int N=in.nextInt();
        int M= in.nextInt();
        int[][] arr=new int[N][M];
        in.nextLine();
        for(int n=0;n<N;n++) {
            String s = in.nextLine();
            for (int m = 0; m < M; m++) {
                if (s.charAt(m) == 'B') {
                    arr[n][m] = 0;
                } else {
                    arr[n][m] = 1;
                }
            }
        }
        int[][] Size=new int[N][M];
        int[] sizes=new int[N*M];
        for(int n=0;n<N;n++) {
            for (int m = 0; m < M; m++) {
                if(arr[n][m]==1) {
                    Size[n][m] = 1;
                    int min = Math.min(n, m);
                    min = Math.min(min, N - n-1);
                    min = Math.min(min, M - m-1);
                    for(int i=1;i<=min;i++){
                        if(arr[n][m-i]==1 && arr[n][m+i]==1 && arr[n-i][m]==1 && arr[n+i][m]==1){
                            Size[n][m] = 1+i;
                        }
                        else{
                            break;
                        }
                    }
                }
                else{
                    Size[n][m] = 0;
                }
            }
        }
        int maxp=0;
        for(int n=3;n<N;n++) {
            for (int m = 2; m < M; m++) {
                if (arr[n][m] == 1) {
                    int s=Size[n][m];
                    for(int n1=6;n1<N;n1++) {
                        for (int m1 = 3; m1 < M; m1++) {
                            if (arr[n1][m1] == 1) {
                                int s1=Size[n1][m1];
                                int p=Product(N,M,n,m,s-1,n1,m1,s1-1);
                                if(p>maxp){
                                    maxp=p;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(maxp);
    }
    public static int Product(int N,int M, int n, int m, int S, int n1, int m1, int S1){
        int max=0;
        for(int s=S;s>=0;s--){
            for(int s1=S1;s1>=0;s1--){
                boolean intersect=false;
                int[][] arr=new int[N][M];
                for(int i=0;i<N;i++){
                    for(int j=0;j<M;j++){
                        arr[i][j]=0;
                    }
                }
                for(int i=n-s;i<=n+s;i++){
                    arr[i][m]=1;
                }
                for(int i=m-s;i<=m+s;i++){
                    if(i!=m) {
                        arr[n][i] = 1;
                    }
                }
                for(int i=n1-s1;i<=n1+s1;i++){
                    if(i<0){
                        i++;
                    }
                    arr[i][m1]+=1;
                    if(arr[i][m1]==2){
                        intersect=true;
                        break;
                    }
                }
                for(int i=m1-s1;i<=m1+s1;i++){
                    if(i!=m1) {
                        arr[n1][i] += 1;
                    }
                    if(arr[n1][i]==2){
                        intersect=true;
                        break;
                    }
                }
                int ar=s*4+1;
                int ar1=s1*4+1;
                int p=ar*ar1;
                if(!intersect && max<p){
                    max=p;
                }
            }
        }
        return max;
    }
}
