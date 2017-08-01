import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n = in.nextInt();
        int d = in.nextInt();
        int[] arr=new int[200];
        int[] arrA=new int[n];
        for(int i=0;i<200;i++){
            arr[i]=0;
        }
        int cnt=0;
        for(int i=0;i<n;i++){
            int v=in.nextInt();
            arrA[i]=v;
            if(i<d){
                arr[v]++;
            }
            else{
                int med=median(arr,d);
                arr[v]++;
                int id=i-d;
                arr[arrA[id]]--;
                if(med<=v){
                    cnt++;
                }
            }
        }
        System.out.print(cnt);
    }
    static int median(int[] arr,int d){
        int med=0,cnt=0;
        for(int i=0;i<arr.length;i++){
            if(d%2==1){
                if(cnt<=d/2 && arr[i]+cnt>d/2){
                    med=2*i;
                    break;
                }
                cnt+=arr[i];
            }
            else{
                if(cnt<d/2 && arr[i]+cnt>d/2){
                    med=i*2;
                    break;
                }
                else if(cnt<d/2 && arr[i]+cnt==d/2){
                    med=(i+i+1);
                    break;
                }
                cnt+=arr[i];
            }
        }
        return med;
    }
}
