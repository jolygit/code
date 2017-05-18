static int height(Node root) {
    // Write your code here.
    int h=0;
    int hL=0,hR=0;
    if(root.left!=null){
	hL=height(root.left);
    } 
    if(root.right!=null){
	hR=height(root.right);
    }
    h=(hL>=hR)?hL:hR;
    if(root.left!=null || root.right!=null){
	return h+1;
    }
    else{
	return 0;
    }
     
}
