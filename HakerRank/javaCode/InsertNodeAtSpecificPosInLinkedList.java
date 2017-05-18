Node InsertNth(Node head, int data, int position) {
if(head==null){
   Node nd=new Node();
   nd.data=data;
   nd.next=null;
   return nd; 
}
else if(position==0){
    Node nd=new Node();
    nd.data=data;
    nd.next=head;
    return nd;
}
else{
    Node before=head;
    for(int i=0;i<position-1;i++){
        before=before.next;
    }
    Node nd=new Node();
    nd.data=data;
    if(before.next!=null){
     nd.next=before.next;
    }
    else{
     nd.next=null;
    }
    before.next=nd;
    return head;
}
}
