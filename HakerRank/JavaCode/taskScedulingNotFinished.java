import java.io.*;
import java.util.*;
class pair {
    pair(int _d,int _m){
        d=_d;
        m=_m;
    }
    public int d;
    public int  m;
}
class list{
    list(int _d,int _m,list _parent){
        d=_d;
        m=_m;
        parent=_parent;
        child=null;
        endtime=m;
        if(endtime-d>maxd){
            maxd=endtime-d;
        }
    }
    list parent;
    list child;
    int d;
    int m;
    int endtime;
    static int maxd;
    static list root;
    void add(int _d,int _m){
        if(_d<=d && parent==null){//updating root only
            if(maxd>0) {
                endtime += _m;
                if (endtime - d > maxd) {
                    maxd = endtime - d;
                }
                if (child != null) {
                    child.update();
                }
            }
            else{
                list nchild =new list(_d,_m,null);
                parent=nchild;
                nchild.child=this;
                root=nchild;
                root.update();
            }
        }
        else if(child==null){
            child =new list(_d,_m,this);
            child.update();
        }
        else if(d<=_d && child.d>_d){
            list nchild =new list(_d,_m,this);
            nchild.child=child;
            child=nchild;
            nchild.update();
        }
        else{
            child.add(_d,_m);
        }
    }
    void update(){
        if(parent!=null) {
            endtime = parent.endtime + m;
        }
        else {
            endtime =  m;
        }
        if(endtime-d>maxd) {
            maxd = endtime - d;
            parent = null;
            root = this;
        }
        if (child != null) {
            child.update();
        }
    }
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
                pair[] pairs=new pair[N];
                for(int n=0;n<N;n++){
                    int d=in.nextInt();
                    int m=in.nextInt();
                    pair pr=new pair(d,m);
                    pairs[n]=pr;
                }
                list root=new list(pairs[0].d,pairs[0].m,null);
                list.root=root;
                System.out.println(list.maxd);
                for(int i=1;i<N;i++){
                    pair pr=pairs[i];
                    list.root.add(pr.d,pr.m);
                    System.out.println(list.maxd);
                    if(list.maxd>0){
                        int stop=0;
                    }
                }
            }
        }

