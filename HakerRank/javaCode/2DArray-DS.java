import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int arr[][] = new int[6][6];
        for(int arr_i=0; arr_i < 6; arr_i++){
            for(int arr_j=0; arr_j < 6; arr_j++){
                arr[arr_i][arr_j] = in.nextInt();
            }
        }
        int max=-10000;
        for(int arr_i=0; arr_i < 6-2; arr_i++){
            for(int arr_j=0; arr_j < 6-2; arr_j++){
                int cnt=0;
                cnt+=arr[arr_i][arr_j];
                cnt+=arr[arr_i][arr_j+1];
                cnt+=arr[arr_i][arr_j+2];
                
                cnt+=arr[arr_i+1][arr_j+1];
                
                cnt+=arr[arr_i+2][arr_j];
                cnt+=arr[arr_i+2][arr_j+1];
                cnt+=arr[arr_i+2][arr_j+2];
                
                if(cnt>max){
                    max=cnt;
                }
                    
            }
        }
        System.out.print(max);
    }
}
