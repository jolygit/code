 public void MyFunction(){
        Node n1=new Node();
        n1.data=1;

        Node n2=new Node();
        n2.data=3;
        n1.next=n2;

        Node n3=new Node();
        n3.data=5;
        n2.next=n3;

        Node n4=new Node();
        n4.data=6;
        n3.next=n4;
        n4.next=null;

        Node l1=new Node();
        l1.data=2;

        Node l2=new Node();
        l2.data=4;
        l1.next=l2;

        Node l3=new Node();
        l3.data=7;
        l2.next=l3;
         l3.next=null;

        Node l4=new Node();
        l4.data=12;
        //l3.next=l4;
        l4.next=null;

        Node sorted=MergeLists(n1,l1
        );
        while(true) {
            if(sorted.next!=null) {
                System.out.println(sorted.data);
                sorted=sorted.next;
            }
            else {
                System.out.println(sorted.data);
                break;
            }
        }
    }

    public Node MergeLists(Node headA,Node headB){
        if(headA==null){
            return headB;
        }
        else if(headB==null){
            return headA;
        }
        else{
            Node head=new Node();
            Node headPos=null;
            Node posA=headA,posB=headB;
            if(posA.data<posB.data){
                head=posA;
                headPos=head;
                posA=posA.next;
            }
            else if(posB.data<posA.data){
                head=posB;
                headPos=head;
                posB=posB.next;
            }
            else if(posB.data==posA.data){
                head=posB;
                posB=posB.next;
                head.next=posA;
                headPos=head.next;
                posA=posA.next;
            }

            while(true){
                if(posA!=null && posB!=null && posA.data<posB.data){
                    headPos.next=posA;
                    headPos=headPos.next;
                    posA=posA.next;
                }
                else if(posA!=null && posB!=null && posA.data>posB.data){
                    headPos.next=posB;
                    headPos=headPos.next;
                    posB=posB.next;
                }
                else if(posA!=null && posB!=null && posA.data==posB.data){
                    headPos.next=posB;
                    headPos=headPos.next;
                    posB=posB.next;
                    headPos.next=posA;
                    headPos=headPos.next;
                    posA=posA.next;
                }
                else if(posA==null && posB!=null){
                    headPos.next=posB;
                    return head;
                }
                else if(posB==null && posA!=null){
                    headPos.next=posA;
                    return head;
                }
            }
        }
