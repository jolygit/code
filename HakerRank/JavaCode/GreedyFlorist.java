
import java.io.*;
import java.util.*;

        public class Solution {

            public static void main(String[] args) {
               
                Scanner in = new Scanner(System.in);

                int n = in.nextInt();
                int k = in.nextInt();
                long[] A = new long[n];
                for(int i=0;i<n;i++){
                    int v = in.nextInt();
                    A[i]=v;
                }
                Arrays.sort(A);
                long cnt=0;
                int iter=n/k;
                int left=n%k;
                int x=0;
                while(iter>0){
                    for(int j=0;j<k;j++) {
                        int idx =n-1-(n/k-iter)*k-j;
                        long val=(n/k-iter+1)*A[idx];
                        cnt+=val;
                    }
                    iter--;
                }
                if(left>0) {
                    for (int j = left; j >0; j--) {
                        long val = (n / k + 1) * A[j-1];
                        cnt += val;
                    }
                }
                System.out.println(cnt) ;
            }
        }
