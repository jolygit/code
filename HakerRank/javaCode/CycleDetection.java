boolean hasCycle(Node head) {
Node p1=head;
Node p2=head;
if(p1==null){
 return false;
}
while(true){
    p1=p1.next;
    p2=p2.next;
    if(p1==null){
      return false;
    }
    p2=p2.next;
    if(p2==null){
      return false;
    }
    if(p1==p2){
     return true;
    }
}
}
