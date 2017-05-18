/*You are given a pointer to the root of a binary tree. You need to print the level order traversal of this tree. In level order traversal, we visit the nodes level by level from left to right. 
You only have to complete the function. 
For example:

         3
       /   \
      5     2
     / \    /
    1   4  6
For the above tree, the level order traversal is 3 -> 5 -> 2 -> 1 -> 4 -> 6.*/

  void LevelOrder(Node root)
    {
      ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();
      CreateLevels(outer,root,0);
      for(int out=0;out<outer.size();out++){
          ArrayList<Integer> inner=outer.get(out);
          for(Integer val:inner){
              System.out.print(val+" ");
          }
      }
    }
   void CreateLevels(ArrayList<ArrayList<Integer>> outer,Node root,int level){
       ArrayList<Integer> inner;
       if(outer.size()<=level){
           inner = new ArrayList<Integer>(); 
           outer.add(inner);
       }
       else{
           inner=outer.get(level);
       }
        inner.add(root.data);
       if(root.left!=null){
           CreateLevels(outer,root.left, level+1);
       }
       if(root.right!=null){
           CreateLevels(outer,root.right, level+1);
       }
   }
