/*You are given an array of  integers which is a permutation of the first  natural numbers. You can swap any two elements of the array. You can make at most  swaps. What is the largest permutation, in numerical order, you can make?

Input Format 
The first line of the input contains two integers,  and , the size of the input array and the maximum swaps you can make, respectively. The second line of the input contains a permutation of the first  natural numbers.

Output Format 
Print the lexicographically largest permutation you can make with at most  swaps.

Constraints 
 

Sample Input#00

5 1
4 2 3 5 1
Sample Output#00

5 2 3 4 1
Explanation#00 
You can swap any two numbers in  and see the largest permutation is 

Sample Input#01

3 1
2 1 3
Sample Output#01

3 1 2
Explanation#01 
With 1 swap we can get ,  and  out of these  is the largest permutation.

Sample Input#02

2 1
2 1
Sample Output#02

2 1
Explanation#02 
    We can see that  is already the largest permutation. So we don't need any swaps.*/
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
class node {
    public int index;
    public long dist;
    public long dist(){return dist;}
    boolean visited;
}
public class Solution {
    public static void main(String[] args) {
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int k = in.nextInt();
        int[] arr=new int[N];
        for(int n = 0; n < N; n++){
            int el = in.nextInt();
            arr[n]=el;
        }
        if(k>=N){
           for(int i=0;i<N;i++){
               System.out.print(N-i+" ");
           }
        }
        else{
            int cnt=k;
            int index=0;
            while(cnt>0){
                if(index==N-1){
                    break;
                }
                if(arr[index]!=(N-index)){
                    int secondIndex=0;
                   for(int j=index+1;j<N;j++){
                       if(arr[j]==(N-index)){
                           int tmp=arr[j];
                           arr[j]=arr[index];
                           arr[index]=tmp;
                           break;
                       }
                   }
                   cnt--;
                }
                index++;
            }
            for(int i=0;i<N;i++){
                System.out.print(arr[i]+" ");
            }
        }
    }
}

