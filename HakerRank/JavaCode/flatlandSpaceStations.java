/*
Flatland is a country with  cities,  of which have space stations. Each city, , is numbered with a distinct index from  to , and each city  is connected to city  by a bidirectional road that is  in length.

For example, if  and cities  and  have space stations, then Flatland looks like this:

hreasy(5).png

For each city, determine its distance to the nearest space station and print the maximum of these distances.

Input Format

The first line consists of two space-separated integers,  and  . 
The second line contains  space-separated integers describing the respective indices of each city having a space-station. These values are unordered and unique.

Constraints

It is guaranteed that there will be at least  city with a space station, and no city has more than one.
Output Format

Print an integer denoting the maximum distance that an astronaut in a Flatland city would need to travel to reach the nearest space station.

Sample Input 0

5 2
0 4
Sample Output 0

2
Explanation 0

This sample corresponds to the example given in the problem statement above. The distance to the nearest space station for each city is listed below:
 */
import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[] arr=new int[n];
        for(int i=0;i<n;i++){
            arr[i]=-1;
        }
        ArrayList<Integer> space=new ArrayList<>();
        for(int i=0;i<m;i++){
            int val=in.nextInt();
            space.add(val);
            arr[val]=0;
        }
        Collections.sort(space);
        for(int mm=0;mm<space.size();mm++){
            if(mm==0){
               for(int i=0;i<=space.get(mm);i++){
                   arr[i]=space.get(mm)-i;
               }
                if(space.size()==1){
                    for(int i=space.get(mm);i<n;i++){
                      arr[i]=i-space.get(mm);
                    }
                }
            }
            else if(mm==space.size()-1){
               for(int i=space.get(mm);i<n;i++){
                   arr[i]=i-space.get(mm);
               }
               for(int i=space.get(mm-1);i<=space.get(mm);i++){
                    arr[i]=Math.min(i-space.get(mm-1),space.get(mm)-i);
                } 
            }
            else{
                for(int i=space.get(mm);i<=space.get(mm+1);i++){
                   arr[i]=Math.min(i-space.get(mm),space.get(mm+1)-i);
               }
            }
        }
       
        int max=-1;
        for(int i=0;i<n;i++){
            if(arr[i]>max){
                max=arr[i];
            }
        }
        System.out.print(max);
    }
}
