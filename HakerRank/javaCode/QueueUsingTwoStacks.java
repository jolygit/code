/*A queue is an abstract data type that maintains the order in which elements were added to it, allowing the oldest elements to be removed from the front and new elements to be added to the rear. This is called a First-In-First-Out (FIFO) data structure because the first element added to the queue (i.e., the one that has been waiting the longest) is always the first one to be removed.

A basic queue has the following operations:

Enqueue: add a new element to the end of the queue.
Dequeue: remove the element from the front of the queue and return it.
In this challenge, you must first implement a queue using two stacks. Then process  queries, where each query is one of the following  types:

1 x: Enqueue element  into the end of the queue.
2: Dequeue the element at the front of the queue.
3: Print the element at the front of the queue.
Input Format

The first line contains a single integer, , denoting the number of queries. 
Each line  of the  subsequent lines contains a single query in the form described in the problem statement above. All three queries start with an integer denoting the query , but only query  is followed by an additional space-separated value, , denoting the value to be enqueued.*/

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
	Stack<Integer> Push1 = new Stack<Integer>();
	Stack<Integer> Push2 = new Stack<Integer>();
	Stack<Integer> Pop = new Stack<Integer>();
	for(int rep=0;rep<N;rep++){
	    int command=in.nextInt();
	    int val;
	    if(command==1){
		val=in.nextInt();
            
		Push1.push(val);
            
	    }
	    else if(command==2){
		if(Pop.empty()){
		    while(!Push1.empty()){
			Pop.push(Push1.pop());
		    }
		}
		Pop.pop();
	    }
	    else if(command==3){
		if(Pop.empty()){
		    while(!Push1.empty()){
			Pop.push(Push1.pop());
		    }
		}
		val=Pop.peek();
		System.out.println(val);
	    }
	}
    }
}

