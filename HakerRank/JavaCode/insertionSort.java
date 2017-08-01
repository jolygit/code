public static void insertIntoSorted(int[] ar) {
        int e=ar[ar.length-1];
        int i=0;
        for(i=ar.length-2;i>=0;i--){
         if(ar[i]>e){
             ar[i+1]=ar[i];
             for(int l=0;l<ar.length;l++){
               System.out.print(ar[l]+" ");
             }
             System.out.println();
         }
         else{
             ar[i+1]=e;
             for(int l=0;l<ar.length;l++){
               System.out.print(ar[l]+" ");
             }
             break;
         }
        }
        
        if(i==-1){
            ar[0]=e;
            for(int l=0;l<ar.length;l++){
               System.out.print(ar[l]+" ");
             }
         
        }
        // Fill up this function  
    }
