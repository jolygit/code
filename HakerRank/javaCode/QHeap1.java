/*
This question is designed to help you get a better understanding of basic heap operations. 
You will be given queries of  types:

" " - Add an element  to the heap.
" " - Delete the element  from the heap.
"" - Print the minimum of all the elements in the heap.
NOTE: It is guaranteed that the element to be deleted will be there in the heap. Also, at any instant, only distinct elements will be in the heap.

Input Format

The first line contains the number of queries, . 
Each of the next  lines contains a single query of any one of the  above mentioned types.

Constraints 
 

Output Format

For each query of type , print the minimum value on a single line.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    Scanner in = new Scanner(System.in);
	int N=in.nextInt();
    PriorityQueue< Integer > prq = new PriorityQueue <> ();
	for(int rep=0;rep<N;rep++){
      int type=in.nextInt();
      if(type==1){
          int val=in.nextInt();
          prq.add(val);
      } 
      else if(type==2){
          int val=in.nextInt();
          prq.remove(val);
      } 
      else{
         int val=prq.peek();
         System.out.println(val);
      }
    }
  }
}
