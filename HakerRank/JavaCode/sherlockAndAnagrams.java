import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    static int sherlockAndAnagrams(String s){
        char[] arr=s.toCharArray();
        int l=arr.length;
        int N=(arr.length*(arr.length+1))/2;
        int[] ss=new int[N];
        for(int i=0;i<N;i++){
            ss[i]=0;
        }
        ArrayList<Integer[]> sets=new ArrayList<>();
        for(int size=1;size<=l;size++){
            for(int j=0;j<=l-size;j++){
                Integer[] cc=new Integer[26];
                for(int sz=0;sz<26;sz++){
                    cc[sz]=0;
                }
                for(int sz=0;sz<size;sz++){
                    char c=arr[j+sz];
                    int idx=((int)c)-97;
                  //  System.out.println(idx);
                    cc[idx]=cc[idx]+(Integer)1;
                }
                sets.add(cc);
            }
        }
        for(int i=0;i<N;i++){
            Integer[] cc=sets.get(i);
            for(int j=i;j<N;j++){
                if(ss[j]==0){
                    Integer[] cj=sets.get(j);
                    boolean same=true;
                    for(int id=0;id<26;id++){
                        if(cc[id]!=cj[id]){
                            same=false;
                            break;
                        }
                    }
                    if(same){
                        ss[j]=i+1;
                    }
                }
            }
        }
        int[] cnt=new int[N];
        for(int i=0;i<N;i++) {
          cnt[i]=0;
        }
        for(int i=0;i<N;i++){
            int id=ss[i]-1;
            cnt[id]++;
        }
        int totCnt=0;
        for(int i=0;i<N;i++){
            int v=cnt[i];
            totCnt+=v*(v-1)/2;
        }
        return totCnt;
        // Complete this function
    }

    public static void main(String[] args) {
       // Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            int result = sherlockAndAnagrams(s);
            System.out.println(result);
        }
    }
}
