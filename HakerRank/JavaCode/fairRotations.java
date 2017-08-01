import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int B[] = new int[N];
        int sum=0;
        for(int B_i=0; B_i < N; B_i++){
            B[B_i] = in.nextInt();
            sum+=B[B_i];
        }
        int sum1=0;
        if(sum%2==0){
            for(int B_i=0; B_i < N; B_i++){
              if(B[B_i]%2==1){
                  B[B_i]++;
                  sum1++;
                  if(B_i<N-1){
                       B[B_i+1]++;
                      sum1++;
                  }
              }
            }
            System.out.print(sum1);
        }
        else{
            System.out.print("NO");
        }
    }
}
