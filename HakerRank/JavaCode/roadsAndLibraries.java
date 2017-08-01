/*
The Ruler of HackerLand believes that every citizen of the country should have access to a library. Unfortunately, HackerLand was hit by a tornado that destroyed all of its libraries and obstructed its roads! As you are the greatest programmer of HackerLand, the ruler wants your help to repair the roads and build some new libraries efficiently.

HackerLand has  cities numbered from  to . The cities are connected by  bidirectional roads. A citizen has access to a library if:

Their city contains a library.
They can travel by road from their city to a city containing a library.
The following figure is a sample map of HackerLand where the dotted lines denote obstructed roads:

image

The cost of repairing any road is  dollars, and the cost to build a library in any city is  dollars.

You are given  queries, where each query consists of a map of HackerLand and value of  and .

For each query, find the minimum cost of making libraries accessible to all the citizens and print it on a new line.

Input Format

The first line contains a single integer, , denoting the number of queries. The subsequent lines describe each query in the following format:

The first line contains four space-separated integers describing the respective values of  (the number of cities),  (the number of roads),  (the cost to build a library), and  (the cost to repair a road).
Each line  of the  subsequent lines contains two space-separated integers,  and , describing a bidirectional road connecting cities  and .
Constraints

Each road connects two distinct cities.
Output Format

For each query, print an integer denoting the minimum cost of making libraries accessible to all the citizens on a new line.

Sample Input

2
3 3 2 1
1 2
3 1
2 3
6 6 2 5
1 3
3 4
2 4
1 2
2 3
5 6
Sample Output

4
12
Explanation

We perform the following  queries:

HackerLand contains  cities connected by  bidirectional roads. The price of building a library is  and the price for repairing a road is . 
image
The cheapest way to make libraries accessible to all is to:

Build a library in city  at a cost of .
Repair the road between cities  and  at a cost of .
Repair the road between cities  and  at a cost of .
This gives us a total cost of . Note that we don't need to repair the road between cities  and because we repaired the roads connecting them to city !

In this scenario it's optimal to build a library in each city because the cost of building a library () is less than the cost of repairing a road (). image

There are  cities, so the total cost is .
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    public static int[] bfs(int root,int[] parent,List< List<Integer> > list){
        Deque<Integer> deque = new ArrayDeque<Integer>();
        Set<Integer> set = new HashSet<Integer>();
        set.add(root);
        deque.add(root);
        parent[root]=-2;//this is the root
        while(!deque.isEmpty()){
            int nd=deque.removeFirst();
            List<Integer> l1=list.get(nd);
            for(int chnode:l1){
                if(!set.contains(chnode)) {
                    deque.add(chnode);
                    set.add(chnode);
                    parent[chnode]=nd;
                }
            }
        }
        return parent;
    }
    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            long n = in.nextInt();
            int m = in.nextInt();
            long x = in.nextInt();
            long y = in.nextInt();

            int[] parents = new int[(int)n];
            for(int p=0;p<n;p++){
                parents[p]=-1;
            }
            List< List<Integer> > edges = new ArrayList<>();
            for(int i=0;i<n;i++){
                List<Integer> lst=new ArrayList<>();
                edges.add(lst);
            }
            int Nedges=m;
            for(int e=0;e<Nedges;e++){
                int c1=in.nextInt()-1;
                int c2=in.nextInt()-1;
                List<Integer> l1=edges.get(c1);
                l1.add(c2);
                List<Integer> l2=edges.get(c2);
                l2.add(c1);
            }
            if(x<=y || n==1 || m==0){
                System.out.println(n*x);
            }
            else {
                //choose the city that does not have parent as the root and build the tree with it as a root
                long numTrees=0;
                for(int i=0;i<n;i++){
                    if(parents[i]==-1){
                        parents=bfs(i,parents,edges);
                        numTrees++;
                    }
                }
                long cost=(n-numTrees)*y+x*numTrees;
                System.out.println(cost);
            }
        }
    }
}
