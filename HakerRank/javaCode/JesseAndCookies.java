/*Jesse loves cookies. He wants the sweetness of all his cookies to be greater than value . To do this, Jesse repeatedly mixes two cookies with the least sweetness. He creates a special combined cookie with:

sweetness  Least sweet cookie   2nd least sweet cookie).

He repeats this procedure until all the cookies in his collection have a sweetness . 
You are given Jesse's cookies. Print the number of operations required to give the cookies a sweetness . Print  if this isn't possible.

Input Format

The first line consists of integers , the number of cookies and , the minimum required sweetness, separated by a space. 
The next line contains  integers describing the array  where  is the sweetness of the  cookie in Jesse's collection.

Constraints

 
 

Output Format

Output the number of operations that are needed to increase the cookie's sweetness . 
Output  if this isn't possible.*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
	int N=in.nextInt();
    int K=in.nextInt();
    PriorityQueue< Integer > prq = new PriorityQueue <> ();
	for(int rep=0;rep<N;rep++){
      int val=in.nextInt();
      prq.add(val);
    }
    int cnt=0;
    while(true){
        Integer min1=prq.poll();
        Integer min2=prq.poll();
        if(min2==null && min1<K){
            cnt=-1;
            break;
        }
      
        if(min1!=null && min2!=null && min1<K){
           int val=min1+2*min2;
           prq.add(val); 
           cnt++;
        }
        else{
            break;
        }
    }
      System.out.println(cnt); 
    }
}
