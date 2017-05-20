/*
For the purposes of this challenge, we define a binary search tree to be a binary tree with the following ordering requirements:

The  value of every node in a node's left subtree is less than the data value of that node.
The  value of every node in a node's right subtree is greater than the data value of that node.
Given the root node of a binary tree, can you determine if it's also a binary search tree?

Complete the function in your editor below, which has  parameter: a pointer to the root of a binary tree. It must return a boolean denoting whether or not the binary tree is a binary search tree. You may have to write one or more helper functions to complete this challenge.

Input Format

You are not responsible for reading any input from stdin. Hidden code stubs will assemble a binary tree and pass its root node to your function as an argument.

Constraints

Output Format

You are not responsible for printing any output to stdout. Your function must return true if the tree is a binary search tree; otherwise, it must return false. Hidden code stubs will print this result as a Yes or No answer on a new line.*/
/* Hidden stub code will pass a root argument to the function below. Complete the function to solve the challenge. Hint: you may want to write one or more helper functions.  

The Node class is defined as follows:
    class Node {
        int data;
        Node left;
        Node right;
     }
*/
    boolean checkBST(Node root) {
        int Max=100000;
        boolean right=check(root.right,root.data,Max);
        boolean left=check(root.left,-1,root.data);
        if(right && left){
            return true;
        }
        else{
            return false;
        }
    }
    boolean check(Node root,int min,int max){
       boolean ret=false;
        if(root==null){
            return true;
        }
        else if(root.data>min && root.data<max){
          boolean right=check(root.right,root.data,max);
          boolean left=check(root.left,min,root.data);
          ret=(right && left);
        }
        else{
            return false;
        }
     return ret;
    }
