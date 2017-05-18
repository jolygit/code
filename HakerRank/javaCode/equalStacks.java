import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n1 = in.nextInt();
        int n2 = in.nextInt();
        int n3 = in.nextInt();
        int h1[] = new int[n1];
        int MaxH1=0,MaxH2=0,MaxH3=0;
        int Heights1[] = new int[n1];
        for(int h1_i=0; h1_i < n1; h1_i++){
            h1[h1_i] = in.nextInt();
            MaxH1+=h1[h1_i];
        }
        int sumSofar=0;
        Heights1[0]=MaxH1;
        for(int h1_i=0; h1_i < n1-1; h1_i++){
            sumSofar+=h1[h1_i];
            Heights1[h1_i+1] = MaxH1-sumSofar;
        }

        int h2[] = new int[n2];
        for(int h2_i=0; h2_i < n2; h2_i++){
            h2[h2_i] = in.nextInt();
            MaxH2+=h2[h2_i];
        }
        sumSofar=0;
        Set<Integer> set2=new HashSet<Integer>();
        set2.add(MaxH2);
        for(int h2_i=0; h2_i < n2-1; h2_i++){
           sumSofar+=h2[h2_i];
           set2.add(MaxH2-sumSofar);
        }

        int h3[] = new int[n3];
        for(int h3_i=0; h3_i < n3; h3_i++){
            h3[h3_i] = in.nextInt();
            MaxH3+=h3[h3_i];
        }
        sumSofar=0;
        Set<Integer> set3=new HashSet<Integer>();
        set3.add(MaxH3);
        for(int h3_i=0; h3_i < n3-1; h3_i++){
           sumSofar+=h3[h3_i];
           set3.add(MaxH3-sumSofar);
        }
        int val=0;
        for(int h1_i=0; h1_i < n1; h1_i++){
           val=Heights1[h1_i];
           if(set2.contains(val) && set3.contains(val)){
               break;
           }
           else{
               val=0;
           }
        }
        System.out.println(val);
    }
}
