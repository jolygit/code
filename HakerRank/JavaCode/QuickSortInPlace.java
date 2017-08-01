import java.io.*;
import java.util.*;

public class Solution {
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
            }

        }
        if(max!=0) {
            arr[st + len] = arr[st + min];
            arr[st + min] = p;
        }
        else{
            min=end-st;
        }
        for(int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
        qsort(arr,st,st+min-1);
        qsort(arr,st+min+1,end);
        return true;
    }
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int N = in.nextInt();
        int[] ar = new int[N];
        for(int i=0;i<N;i++){
            ar[i]=in.nextInt();
        }
        qsort(ar,0,ar.length-1);
    }
}
