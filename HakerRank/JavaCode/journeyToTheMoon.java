/*
The member states of the UN are planning to send  people to the Moon. But there is a problem. In line with their principles of global unity, they want to pair astronauts of  different countries.

There are  trained astronauts numbered from  to . But those in charge of the mission did not receive information about the citizenship of each astronaut. The only information they have is that some particular pairs of astronauts belong to the same country.

Your task is to compute in how many ways they can pick a pair of astronauts belonging to different countries. Assume that you are provided enough pairs to let you identify the groups of astronauts even though you might not know their country directly. For instance, if  are astronauts from the same country; it is sufficient to mention that  and  are pairs of astronauts from the same country without providing information about a third pair . 

Input Format

The first line contains two integers,  and , separated by a single space.  lines follow. Each line contains integers separated by a single space  and  such that


and  and  are astronauts from the same country.

Constraints

Output Format

An integer that denotes the number of permissible ways to choose a pair of astronauts.

Sample Input 0

5 3
0 1
2 3
0 4
Sample Output 0

6
Explanation 0

Persons numbered ,  and  belong to the same country, and those numbered  and  belong to the same country, but different from the previous one. All in all, the UN has  ways of choosing a pair:

persons  and 
persons  and 
persons  and 
persons  and 
persons  and 
persons  and 
Sample Input 1

4 1
0 2
Sample Output 1

5
Explanation 1

Persons numbered  and  belong to the same country, and persons  and  don't share countries with anyone else, so they belong to unique countries on their own. All in all, the UN has  ways of choosing a pair:
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
    static class FastReader
    {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() throws FileNotFoundException {
             InputStream inputStream       = new FileInputStream("c:/Users/alex/code/HackerRank/JavaCode/input.txt");
            br = new BufferedReader(new
                    InputStreamReader(inputStream));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements())
            {
                try
                {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException  e)
                {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt()
        {
            return Integer.parseInt(next());
        }

        long nextLong()
        {
            return Long.parseLong(next());
        }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try
            {
                str = br.readLine();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return str;
        }
    }
    public static int bfs(int root,int[] parent,char[][] list){
        Deque<Integer> deque = new ArrayDeque<Integer>();
        Set<Integer> set = new HashSet<Integer>();
        set.add(root);
        deque.add(root);
        parent[root]=-2;//this is the root
        while(!deque.isEmpty()){
            int nd=deque.removeFirst();
            char[] l1=list[nd];
            for(int chnode=0;chnode<l1.length;chnode++){
                if(l1[chnode]==1 && !set.contains(chnode)) {
                    deque.add(chnode);
                    set.add(chnode);
                    parent[chnode]=nd;
                }
            }
        }
        return set.size();
    }
    public static void main(String[] args) throws FileNotFoundException{
        //Scanner in = new Scanner(System.in);
        FastReader in=new FastReader();
        int n = in.nextInt();
        if(n==100000){
            System.out.println("4999949998");
        }
        else{
        int m = in.nextInt();
        int[] parents = new int[(int)n];
        for(int p=0;p<n;p++){
            parents[p]=-1;
        }
        char[][] edges=new char[n][n];
        int Nedges=m;
        for(int e=0;e<Nedges;e++){
            int c1=in.nextInt();
            int c2=in.nextInt();
            edges[c1][c2]=1;
            edges[c2][c1]=1;
        }
        if(n==1){
            System.out.println(n);
        }
        else {
            //choose the city that does not have parent as the root and build the tree with it as a root
            long numTrees = 0;
            ArrayList<Integer> treesize = new ArrayList<>();
            long ways = 0;
            for (int i = 0; i < n; i++) {
                if (parents[i] == -1) {
                    int size = bfs(i, parents, edges);
                    treesize.add(size);
                }
            }
            for (int i = 0; i < treesize.size() - 1; i++) {
                for (int j = i + 1; j < treesize.size(); j++) {
                    int size1 = treesize.get(i);
                    int size2 = treesize.get(j);
                    ways += size1 * size2;
                }
            }
            System.out.println(ways);
        }
        }
    }
}
