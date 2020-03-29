//============================================================================================================================
// Queue.java
// A Queue ADT built using a linked list.
// Tristan Clark
//============================================================================================================================

public class Queue implements QueueInterface {

    // Node Constructor
    private class Node {
        Object obj;
        Node next;

        Node(Object x){
            obj = x;
            next = null;
        }
    }
    
    // private variables
    private int len;
    private Node front;
    private Node back;

    // Queue Constructor
    public Queue(){
        len = 0;
        front = null;
        back = null;
    }
    
    //------------------------------------------------------------------------------------------------------------------------
    // Accessors
    //------------------------------------------------------------------------------------------------------------------------
    
    // isEmpty()
    // pre: none
    // post: returns true if this Queue is empty, false otherwise
    public boolean isEmpty() {return (len == 0);}
 
    // length()
    // pre: none
    // post: returns the length of this Queue.
    public int length() { return len;}
 
    //------------------------------------------------------------------------------------------------------------------------
    // Manipulators
    //------------------------------------------------------------------------------------------------------------------------

    // enqueue()
    // adds newItem to back of this Queue
    // pre: none
    // post: !isEmpty()
    public void enqueue(Object newItem) {
        Node N = new Node(newItem);
        if(len == 0) {
            front = N;
            back = N;
        } else {
            back.next = N;
            back = N;
        }
        len++;
    }
 
    // dequeue()
    // deletes and returns item from front of this Queue
    // pre: !isEmpty()
    // post: this Queue will have one fewer element
    public Object dequeue() throws QueueEmptyException {
        if(!isEmpty()) {
            Node N = front;
            if(len == 1) {
                back = null;
                front = null;
            } else {
                front = front.next;
            }
            len--;
            return N.obj;
        } else {
            throw new QueueEmptyException("Queue Error: Dequeue() called on empty Queue.");
        }
    }
 
    // peek()
    // pre: !isEmpty()
    // post: returns item at front of Queue
    public Object peek() throws QueueEmptyException {
        if(!isEmpty()){
            return front.obj;
        } else {
            throw new QueueEmptyException("Queue Error: peek() called on empty Queue.");
        }
    }
 
    // dequeueAll()
    // sets this Queue to the empty state
    // pre: !isEmpty()
    // post: isEmpty()
    public void dequeueAll() throws QueueEmptyException {
        if(!isEmpty()) {
            front = null;
            back = null;
            len = 0;
        } else {
            throw new QueueEmptyException("Queue Error: dequeueAll() called on empty Queue.");
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
    // Other Methods
    //------------------------------------------------------------------------------------------------------------------------
 
    // toString()
    // overrides Object's toString() method
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Node N = front;
        for(;N != null; N = N.next) {
            sb.append(N.obj).append(" ");
        }
        return new String(sb);
    }

}