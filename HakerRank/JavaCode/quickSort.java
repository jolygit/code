import java.util.*;
public class Solution {
       
          static void quickSort(int[] ar) {
              ArrayList<Integer> arr=new ArrayList();      
              for(Integer el:ar){
                arr.add(el);
              }
             qSort(arr);
          }
          static ArrayList<Integer> qSort(ArrayList<Integer> ar) {
              ArrayList<Integer> eq=new ArrayList();      
              ArrayList<Integer> less=new ArrayList();      
              ArrayList<Integer> more=new ArrayList();      
              int piv=ar.get(0);
              for(Integer el:ar){
                  if(el==piv){
                      eq.add(el);
                  }
                  else if(el>piv){
                      more.add(el);
                  }
                  else{
                      less.add(el);
                  }
             }
             if(less.size()>0){
             less=qSort(less);
             }
             if(more.size()>0){
            more= qSort(more);
             }
             ar=less;
             ar.addAll(eq);
             ar.addAll(more);
             if(ar.size()>1){
             for(Integer el:ar){
                 System.out.print(el+" ");
             }
             System.out.println();
             }
            return ar;
          }
 
      public static void main(String[] args) {
           Scanner in = new Scanner(System.in);
           int n = in.nextInt();
           int[] ar = new int[n];
           for(int i=0;i<n;i++){
              ar[i]=in.nextInt(); 
           }
           quickSort(ar);
       }    
   }
