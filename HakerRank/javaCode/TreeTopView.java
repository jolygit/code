/*
You are given a pointer to the root of a binary tree. Print the top view of the binary tree. 
You only have to complete the function. 
For example :

     3
   /   \
  5     2
 / \   / \
1   4 6   7
 \       /
  9     8
Top View : 1 -> 5 -> 3 -> 2 -> 7
Input Format

You are given a function,*/
void top_view(Node root)
{
         if(root.left!=null){
             PLeft(root.left);
         }
         
         if(root!=null){
             System.out.print(root.data+" ");
         }
         if(root.right!=null){
             PRight(root.right);
         }
}
void PLeft(Node root){
         if(root.left!=null){
             PLeft(root.left);
         }
         System.out.print(root.data+" ");
}
void PRight(Node root){
        System.out.print(root.data+" ");
         if(root.right!=null){
             PRight(root.right);
         }
       
}
