//this does not pass the tests 
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}
class SumInLeavesVisitor extends TreeVis {
    public int getResult() {
        //implement this
        return res;
    }

    public void visitNode(TreeNode node) {
        //implement this
    }

    public void visitLeaf(TreeLeaf leaf) {
        res+=leaf.getValue();
        //implement this
    }
    private int res=0;
}

class ProductOfRedNodesVisitor extends TreeVis {
    public int getResult() {
        //implement this
        return (int)res;
    }

    public void visitNode(TreeNode node) {
        if(node.getColor().equals(Color.RED)){
            res=(res*node.getValue())%(1000000000+7);
        }
        //implement this
    }

    public void visitLeaf(TreeLeaf leaf) {
        if(leaf.getColor().equals(Color.RED)){
            res=(res*leaf.getValue())%(1000000000+7);
        }
        //implement this
    }
    long res=1;
}

class FancyVisitor extends TreeVis {
    public int getResult() {
        //implement this
        return (dp-green)>=0?(dp-green):(green-dp);
        //return dp;
    }

    public void visitNode(TreeNode node) {
        int d=node.getDepth();
        if( d%2==0){
            dp+=node.getValue();
        }
        //implement this
    }

    public void visitLeaf(TreeLeaf leaf) {
        if(leaf.getColor().equals(Color.GREEN)){
            green+=leaf.getValue();

        }
        //implement this
    }
    int green=0;
    int dp=0;
}

public class Solution {

    public static Tree solve() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("c:/Users/alex/code/HackerRank/JavaCode/input.txt"));//System.in);
        int N = scan.nextInt();
        boolean[] isLeaf=new boolean[N];
        int[] nodeValues=new int[N];
        Tree[] nodes=new Tree[N];
        for (int i=0;i<N;i++){
            isLeaf[i]=true;
            int n = scan.nextInt();
            nodeValues[i]=n;
        }
        int[] color=new int[N];
        for (int i=0;i<N;i++){
            int n = scan.nextInt();
            color[i]=n;
        }
        int[] parent=new int[N];
        for (int i=0;i<N;i++){
            parent[i]=-1;//the first node is the root
        }

        for (int i=0;i<N-1;i++){
            int n1 = scan.nextInt();
            int n2 = scan.nextInt();
            n1--;
            n2--;
            if(n1==0){
                parent[n2]=n1;
                isLeaf[n1]=false;
            }
            else if(n2==0){
                parent[n1]=n2;
                isLeaf[n2]=false;
            }
            else{
                if(parent[n1]==-1){
                    parent[n1]=n2;
                    isLeaf[n2]=false;
                }
                else{
                    parent[n2]=n1;
                    isLeaf[n1]=false;
                }
            }

        }
        for (int i=0;i<N;i++){
            nodes[i]=null;
        }
        for (int i=0;i<N;i++){

            int depth=0;
            int index=i;
            int value=nodeValues[i];
            while(parent[index]!=-1){
                depth++;
                index=parent[index];
            }
            Color clr=color[i]>0?Color.GREEN:Color.RED;
            if(!isLeaf[i]){
                TreeNode nd;
                if(nodes[i]==null){
                    nd=new TreeNode(value,clr,depth);
                    nodes[i]=nd;
                }
                else{
                    nd=(TreeNode)nodes[i];
                }
                int pi=parent[i];
                if(pi!=-1){//
                    if(nodes[pi]!=null){
                        TreeNode parentnd=(TreeNode)nodes[pi];
                        parentnd.addChild(nd);
                    }
                    else{//I think we should not get to here ever
                        int dp=0;
                        int index1=pi;
                        int vl=nodeValues[pi];
                        while(parent[index1]!=-1){
                            dp++;
                            index1=parent[index1];
                        }
                        Color clp=color[pi]>0?Color.GREEN:Color.RED;
                        TreeNode trnd=new TreeNode(vl,clp,dp);
                        trnd.addChild(nd);
                        nodes[pi]=trnd;
                    }
                }
            }
            else if(isLeaf[i]){
                TreeLeaf lf=new TreeLeaf(value,clr,depth);
                nodes[i]=lf;
                int pi=parent[i];

                if(nodes[pi]!=null){
                    ((TreeNode)nodes[pi]).addChild(lf);
                }
                else{
                    int dp=0;
                    int index1=pi;
                    int vl=nodeValues[pi];
                    while(parent[index1]!=-1){
                        dp++;
                        index1=parent[index1];
                    }
                    Color clp=color[pi]>0?Color.GREEN:Color.RED;
                    TreeNode trnd=new TreeNode(vl,clp,dp);
                    trnd.addChild(lf);
                    nodes[pi]=trnd;
                }
            }

        }
        return nodes[0];
        //read the tree from STDIN and return its root as a return value of this function
    }

    public static void main(String[] args) {
        Tree root = null;
        try {
            root = solve();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
