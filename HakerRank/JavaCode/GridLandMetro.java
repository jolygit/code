
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
class pair implements Comparable<pair>{
    pair(long _st,long _end){
        st=_st;
        end=_end;
    }
   @Override
   public int compareTo(pair other){
        if(st<other.st){
            return -1;
        }
        else if(st>other.st){
            return 1;
        }
        else {
            return 0;
        }
    }
    public long st;
    public long  end;
}
public class Solution {

    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long n = in.nextLong();
        long m = in.nextLong();
        long k = in.nextLong();
        Map<Long,ArrayList<pair>> map=new HashMap<>();
        for(int i=0;i<k;i++){
            long r = in.nextLong();
            long c1 = in.nextLong();
            long c2 = in.nextLong();
            pair pr=new pair(c1,c2);
            if(map.get(r)!=null) {
                ArrayList<pair> arr=map.get(r);
                arr.add(pr);
            }
            else{
                ArrayList<pair> arr=new ArrayList<>();
                arr.add(pr);
                map.put(r,arr);
            }
        }
        Iterator it = map.entrySet().iterator();
        long cnt=0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            ArrayList<pair> arr=(ArrayList<pair> )entry.getValue();
            Collections.sort(arr);
            cnt+=arr.get(0).end-arr.get(0).st+1;
            long end=arr.get(0).end;
            for(int i=1;i<arr.size();i++){
                pair v=arr.get(i);
                long st=Math.max(end+1,v.st);
                long ed=v.end;
                if(st<=ed){
                    cnt+=ed-st+1;
                }
               end=Math.max(end,ed);
            }
            it.remove();
        }
        System.out.print(n*m-cnt);
    }
}
