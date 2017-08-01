/*
You are given a square map of size . Each cell of the map has a value denoting its depth. We will call a cell of the map a cavity if and only if this cell is not on the border of the map and each cell adjacent to it has strictly smaller depth. Two cells are adjacent if they have a common side (edge).

You need to find all the cavities on the map and depict them with the uppercase character X.

Input Format

The first line contains an integer, , denoting the size of the map. Each of the following  lines contains  positive digits without spaces. Each digit (1-9) denotes the depth of the appropriate area.

Constraints 

Output Format

Output  lines, denoting the resulting map. Each cavity should be replaced with character X.

Sample Input

4
1112
1912
1892
1234
Sample Output

1112
1X12
18X2
1234
Explanation

The two cells with the depth of 9 fulfill all the conditions of the Cavity definition and have been replaced by X.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] grid = new String[n];
        
        for(int grid_i=0; grid_i < n; grid_i++){
            grid[grid_i] = in.next();
        }
        int m=grid[0].length();
        int[][] map=new int[n][m];
        for(int grid_i=0; grid_i < n; grid_i++){
           String st=grid[grid_i];
           for(int i=0;i<st.length();i++){
               char c=st.charAt(i);
               int v=Character.getNumericValue(c);
               map[grid_i][i]=v;
           }
        }
        for(int grid_i=1; grid_i < n-1; grid_i++){
          for(int i=1;i<m-1;i++){
              int v=map[grid_i][i];
              int l=map[grid_i][i-1];
              int r=map[grid_i][i+1];
              int u=map[grid_i-1][i];
              int d=map[grid_i+1][i];
              if(v>l && v>r && v>u && v>d){
                 String st=grid[grid_i];
                  st=st.substring(0,i)+"X"+st.substring(i+1);
                  grid[grid_i]=st;
              }
          }
        }
         for(int grid_i=0; grid_i < n; grid_i++){
             System.out.println(grid[grid_i]);
         }
    }
}
