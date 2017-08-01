import java.io.*;
import java.util.*;

public class Solution {
public static void insertIntoSorted(int[] ar,int len) {
        int e=ar[len-1];
        int i=0;
        for(i=len-2;i>=0;i--){
         if(ar[i]>e){
             ar[i+1]=ar[i];
         }
         else{
             ar[i+1]=e;
             break;
         }
        }
        
        if(i==-1){
            ar[0]=e;
        }
        // Fill up this function  
    }
    public static void insertionSortPart2(int[] ar)
    {       
        for(int i=1;i<ar.length;i++){
          insertIntoSorted(ar,i+1);
          for(int l=0;l<ar.length;l++){
               System.out.print(ar[l]+" ");
          }
          System.out.println();
        }
           // Fill up the code for the required logic here
           // Manipulate the array as required
           // The code for Input/Output is already provided
    }  
    
    
      
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
       int s = in.nextInt();
       int[] ar = new int[s];
       for(int i=0;i<s;i++){
            ar[i]=in.nextInt(); 
       }
       insertionSortPart2(ar);    
                    
    }    
    private static void printArray(int[] ar) {
      for(int n: ar){
         System.out.print(n+" ");
      }
        System.out.println("");
   }
}
