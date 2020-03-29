//============================================================================================================================
// QueueTest.java
// This program will be used to test the Queue ADT as I am building it.
// Tristan Clark
//============================================================================================================================

public class QueueTest {
    public static void main(String args[]){
        Queue Q = new Queue();
        //System.out.println(Q.isEmpty());
        //System.out.println(Q.length());
        
        Q.enqueue(12);
        //System.out.println(Q.isEmpty());
        //System.out.println(Q.length());
        System.out.println(Q);

        Q.enqueue("newItem");
        System.out.println(Q.length());
        System.out.println(Q);

        Q.enqueue(2.4);
        Q.enqueue("somethingelse");
        System.out.println(Q.length());
        System.out.println(Q);

        //Q.dequeue();
        //Q.dequeue();
        //Q.dequeue();
        //Q.dequeue();
        //System.out.println(Q.length());
        //System.out.println(Q);

        //System.out.println(Q.peek());
        //Q.dequeue();
        //System.out.println(Q.peek());

        Q.dequeueAll();
        System.out.println(Q.isEmpty());
        System.out.println(Q);
        //Q.dequeueAll();

    }
}