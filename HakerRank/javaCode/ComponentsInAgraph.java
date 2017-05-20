/*There are  values to represent nodes in a graph. They are divided into two sets  and . Each set has exactly  values. Set  is represent by .  can contain any value between  to (inclusive). Set  is represented by .  can contain any value between  to (inclusive). Same value can be chosen any number of times.

Here  represents the edges of the graph.

Your task is to print the number of vertices in the smallest and the largest connected components of the graph.

Note Single nodes should not be considered in the answer.

For more clarity look at the following figure.

Alt text

For the above graph smallest connected component is 7 and largest connected component is 17.*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
class Node{
         Node parent;
         int rank;
         int size;
     }

     
public class Solution {
  
static  Node[] nodes = new Node[15000*2];

    public static void main(String[] args) {
      
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
	Scanner in = new Scanner(System.in);
	int N=in.nextInt();
    for(int n=0;n<2*N;n++){
       nodes[n]=null;
    }
      for(int n=0;n<N;n++){
        int v1=in.nextInt()-1;
        int v2=in.nextInt()-1;
         if(nodes[v1]==null){
            Node nd=new Node();
            nd.parent=nd;
            nd.rank=0;
            nd.size=1;
            nodes[v1]=nd;
         }
         if(nodes[v2]==null){
            Node nd=new Node();
            nd.parent=nd;
            nd.rank=0;
            nd.size=1;
            nodes[v2]=nd;
         }
        Node n1=nodes[v1];
        Node n2=nodes[v2];
        Node s1=FindSet(n1);
        Node s2=FindSet(n2);
        if(s1!=s2){
            Union(s1,s2,v1,v2);
        }
      }
     int min=100000,max=-1;
    for(int n=0;n<2*N;n++){
       if(nodes[n]!=null){
            Node nd=nodes[n];
            while(nd.parent!=nd){
             nd=nd.parent;
            }
            if(nd.size>max){
             max=nd.size;
            }
            if(nd.size<min){
             min=nd.size;
            }
      }
    }
    if(N==1){
    System.out.print(2+" "+2);
    }
    else{
    System.out.print(min+" "+max);
    }
    }
 public static Node FindSet(Node nd){//nd is not null for sure
     if(nd.parent!=nd){
         nd.parent=FindSet(nd.parent);
     }
   return nd.parent;
 }
 public static void Union(Node s1,Node s2,int v1,int v2){
     if(s1.rank>s2.rank){
         s2.parent=s1;
         s1.size+=s2.size;
         nodes[v2]=s1;
         nodes[v1]=s1;
     }
     else if(s2.rank>s1.rank){
         s1.parent=s2;
         s2.size+=s1.size;
         nodes[v1]=s2;
         nodes[v2]=s2;
     }
     else{
        s1.parent=s2;
        s2.rank++;
        s2.size+=s1.size;
        nodes[v1]=s2;
        nodes[v2]=s2;
     }
 }
}
