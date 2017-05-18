/*
Suppose there is a circle. There are  petrol pumps on that circle. Petrol pumps are numbered  to  (both inclusive). You have two pieces of information corresponding to each of the petrol pump: (1) the amount of petrol that particular petrol pump will give, and (2) the distance from that petrol pump to the next petrol pump.

Initially, you have a tank of infinite capacity carrying no petrol. You can start the tour at any of the petrol pumps. Calculate the first point from where the truck will be able to complete the circle. Consider that the truck will stop at each of the petrol pumps. The truck will move one kilometer for each litre of the petrol.

Input Format

The first line will contain the value of .
The next  lines will contain a pair of integers each, i.e. the amount of petrol that petrol pump will give and the distance between that petrol pump and the next petrol pump.

Constraints:


Output Format

An integer which will be the smallest index of the petrol pump from which we can start the tour.
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
    ArrayDeque<Integer> Alldeque = new ArrayDeque<>();
	for(int rep=0;rep<N;rep++){
     int gas=in.nextInt();
     int dist=in.nextInt();
     int val=gas-dist;
     Alldeque.push(val);
    }
     int cnt=0,index=0;
     ArrayDeque<Integer> Subdeque = new ArrayDeque<>();
    for(int rep=0;rep<N;rep++){
      int val=Alldeque.pollLast();
      Alldeque.push(val);
      Subdeque.push(val);
      cnt+=val;
      while(cnt<0){
          index++;
          int popval=Subdeque.pollLast();
          cnt-=popval;
      }
    }
        System.out.println(index);
  }
}
