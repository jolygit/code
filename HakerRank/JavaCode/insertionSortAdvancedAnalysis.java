/*
Count inversions in an array | Set 3 (Using BIT)
Inversion Count for an array indicates ¡V how far (or close) the array is from being sorted. If array is already sorted then inversion count is 0. If array is sorted in reverse order that inversion count is the maximum.

     Two elements a[i] and a[j] form an inversion if 
     a[i] > a[j] and i < j. For simplicity, we may 
     assume that all elements are unique.

     Example:
     Input:  arr[] = {8, 4, 2, 1}
     Output: 6
     Given array has six inversions (8,4), (4,2),
     (8,2), (8,1), (4,1), (2,1).     


We strongly recommend that you click here and practice it, before moving on to the solution.


We have already discussed below methods to solve inversion count
1) Naive and Modified Merge Sort
2) Using AVL Tree

Background:
BIT basically supports two operations for an array arr[] of size n:

Sum of elements till arr[i] in O(Log n) time.
Update an array element in O(Log n) time.
BIT is implemented using an array and works in form of trees. Note that there are two ways of looking at BIT as a tree.

The sum operation where parent of index x is "x - (x & -x)".
The update operation where parent of index x is "x + (x & -x)".
We recommend you to refer Binary Indexed Tree (BIT) before further reading this post.

Basic Approach using BIT of size £K(maxElement):
The idea is to iterate the array from n-1 to 0. When we are at i'th index, we check how many numbers less than arr[i] are present in BIT and add it to the result. To get the count of smaller elements, getSum() of BIT is used. In his basic idea, BIT is represented as an array of size equal to maximum element plus one. So that elements can be used as an index.
After that we add current element to to the BIT[] by doing an update operation that updates count of current element from 0 to 1, and therefore updates ancestors of current element in BIT (See update() in BIT for details).

Below is C++ implementation of basic idea that uses BIT.

// C++ program to count inversions using Binary Indexed Tree
#include<bits/stdc++.h>
using namespace std;
 
// Returns sum of arr[0..index]. This function assumes
// that the array is preprocessed and partial sums of
// array elements are stored in BITree[].
int getSum(int BITree[], int index)
{
    int sum = 0; // Initialize result
 
    // Traverse ancestors of BITree[index]
    while (index > 0)
    {
        // Add current element of BITree to sum
        sum += BITree[index];
 
        // Move index to parent node in getSum View
        index -= index & (-index);
    }
    return sum;
}
 
// Updates a node in Binary Index Tree (BITree) at given index
// in BITree.  The given value 'val' is added to BITree[i] and
// all of its ancestors in tree.
void updateBIT(int BITree[], int n, int index, int val)
{
    // Traverse all ancestors and add 'val'
    while (index <= n)
    {
       // Add 'val' to current node of BI Tree
       BITree[index] += val;
 
       // Update index to that of parent in update View
       index += index & (-index);
    }
}
 
// Returns inversion count arr[0..n-1]
int getInvCount(int arr[], int n)
{
    int invcount = 0; // Initialize result
 
    // Find maximum element in arr[]
    int maxElement = 0;
    for (int i=0; i<n; i++)
        if (maxElement < arr[i])
            maxElement = arr[i];
 
    // Create a BIT with size equal to maxElement+1 (Extra
    // one is used so that elements can be directly be
    // used as index)
    int BIT[maxElement+1];
    for (int i=1; i<=maxElement; i++)
        BIT[i] = 0;
 
    // Traverse all elements from right.
    for (int i=n-1; i>=0; i--)
    {
        // Get count of elements smaller than arr[i]
        invcount += getSum(BIT, arr[i]-1);
 
        // Add current element to BIT
        updateBIT(BIT, maxElement, arr[i], 1);
    }
 
    return invcount;
}
 
// Driver program
int main()
{
    int arr[] = {8, 4, 2, 1};
    int n = sizeof(arr)/sizeof(int);
    cout << "Number of inversions are : " << getInvCount(arr,n);
    return 0;
}
Run on IDE
Output:
 */
import java.io.*;
import java.util.*;

public class Solution {
    static long getSum(int[] bit,int index)
    {
        long cnt=0;
        while(index>0){
            cnt+=(long)bit[index];
            index-=index&(-index);
        }
        return cnt;
    }
    static void add(int[] bit,int max,int val,int index){
        while(index<=max){
            bit[index]+=val;
            index+=index&(-index);
        }
    }
    public static void main(String[] args) {
        //Scanner in = new Scanner(System.in);
        Scanner in = null;
        try {
            in = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int T = in.nextInt();
        for(int t=0;t<T;t++) {
            int N = in.nextInt();
            long[] arr = new long[N];
            int max=-1;
            for (int i = 0; i < N; i++) {
                long v = in.nextLong();
                arr[i] = v;
                if(max<v){
                    max=(int)v;
                }
            }
            int[] bit=new int[max+1];
            long cnt=0;
            for(int i=N-1;i>=0;i--){
                int v=(int)arr[i];
                cnt+=getSum(bit,v-1);
                add(bit,max,1,v);
            }
            System.out.println(cnt);
        }

    }
}
