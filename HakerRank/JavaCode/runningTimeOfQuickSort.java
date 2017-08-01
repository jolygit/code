
import java.io.*;
import java.util.*;

public class Solution {
    public static int insertIntoSorted(int[] ar,int len) {
        int cnt=0;
        int e=ar[len-1];
        int i=0;
        for(i=len-2;i>=0;i--){
            if(ar[i]>e){
                ar[i+1]=ar[i];
                cnt++;
            }
            else{
                ar[i+1]=e;
                break;
            }
        }

        if(i==-1){
            ar[0]=e;
        }
        return cnt;
        // Fill up this function
    }
    public static int insertionSortPart2(int[] ar)
    {
        int cnt=0;
        for(int i=1;i<ar.length;i++){
            cnt+=insertIntoSorted(ar,i+1);
        }
        return cnt;
    }



    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int s = in.nextInt();
        int[] ar = new int[s];
        int[] ar1 = new int[s];
        for(int i=0;i<s;i++){
            ar[i]=in.nextInt();
            ar1[i]=ar[i];
        }
        int cntIns=insertionSortPart2(ar);
        qsort(ar1,0,ar1.length-1);
        System.out.println(cntIns-cntQS);
    }
    static int cntQS=0;

    public static boolean qsort(int[] arr,int st,int end){
        int len=end-st;
        if(len<=0){
            return true;
        }
        int min=0,max=0;
        int p=arr[st+len];
        for(int i=0;i<len;i++){
            if(arr[st+i]>p && min==max){
                max=i+1;
                min=i;
            }
            else if(arr[st+i]>p){
                max++;
            }

            if(arr[st+i]<p && min!=max){
                int tmp=arr[st+i];
                arr[st+i]=arr[st+min];
                arr[st+min]=tmp;
                min++;
                max++;
                cntQS++;
            }
            else if(arr[st+i]<p){
                cntQS++;
            }
        }
        if(max!=0) {
            arr[st + len] = arr[st + min];
            arr[st + min] = p;
        }
        else{
            min=end-st;
        }
        cntQS++;
        /*for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();*/
        qsort(arr,st,st+min-1);
        qsort(arr,st+min+1,end);
        return true;
    }
}
