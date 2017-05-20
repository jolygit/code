package com.jolytech.sample.hackerrankproblems;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayDeque;
import java.util.LinkedList;
class Node {
    int data;
    Node next;
}
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] lst={5,6,3,1,2,4};
        int n=6,v1=2,v2=4;
        setContentView(R.layout.activity_main);
        int D=bstDistance(lst,n,v1,v2);
        System.out.print(D);
    }
    class Node{
        int data;
        Node left;
        Node right;
    }
    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public int bstDistance(int[] values, int n,
                           int node1, int node2)
    {
        boolean exist1=false,exist2=false;
        Node root=new Node();
        root.data=values[0];
        root.left=null;
        root.right=null;
        for(int v=1;v<n;v++){
            int val=values[v];
            Node node=new Node();
            node.data=val;
            node.left=null;
            node.right=null;
            insert(node,root);
            if(node1==val){
                exist1=true;
            }
            if(node2==val){
                exist2=true;
            }
        }
        if(!exist1 || !exist2){
            return -1;
        }
        else{
            int D= findDist(root,node1,node2);
            return D;
        }
    }
    public int findDist(Node root,int node1,int node2){
        int D=0;
        if(node1<root.data && node2>root.data){
            int l1=findH(root,node1);
            int l2=findH(root,node2);
            D=l1+l2;
        }
        else if(node1==root.data){
            int l2=findH(root,node2);
            D=l2;
        }
        else if(node2==root.data){
            int l1=findH(root,node1);
            D=l1;
        }
        else if(node1<root.data && node2<root.data){
            D=findDist(root.left,node1,node2);

        }
        else if(node1>root.data && node2>root.data){
            D=findDist(root.right,node1,node2);

        }
        return D;
    }
    public int findH(Node root, int val){
        int H=0;
        if(root.data==val){
            H=0;
        }
        else if(root.data<val){
            H=findH(root.right,val)+1;
        }
        else if(root.data>val){
            H=findH(root.left,val)+1;
        }
        return H;
    }
    public void insert(Node node,Node root){
        if(node.data<root.data){
            if(root.left==null){
                root.left=node;
            }
            else{
                insert(node,root.left);
            }
        }
        else if(node.data>=root.data){
            if(root.right==null){
                root.right=node;
            }
            else{
                insert(node,root.right);
            }
        }
    }

}
