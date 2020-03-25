//============================================================================================================================
// Queue.java
// A Queue ADT built using a linked list.
// Tristan Clark
//============================================================================================================================

public class Queue implements QueueInterface {

    // private variables

    // Node Constructor

    // Queue Constructor
    
    //------------------------------------------------------------------------------------------------------------------------
    // Accessors
    //------------------------------------------------------------------------------------------------------------------------
    
    // isEmpty()
    // pre: none
    // post: returns true if this Queue is empty, false otherwise
    public boolean isEmpty(){
        return true;
    }
 
    // length()
    // pre: none
    // post: returns the length of this Queue.
    public int length() {
        int x = 0;
        return x;
    }
 
    //------------------------------------------------------------------------------------------------------------------------
    // Manipulators
    //------------------------------------------------------------------------------------------------------------------------

    // enqueue()
    // adds newItem to back of this Queue
    // pre: none
    // post: !isEmpty()
    public void enqueue(Object newItem) {

    }
 
    // dequeue()
    // deletes and returns item from front of this Queue
    // pre: !isEmpty()
    // post: this Queue will have one fewer element
    public Object dequeue() throws QueueEmptyException {
        Object x;
        return x;
    }
 
    // peek()
    // pre: !isEmpty()
    // post: returns item at front of Queue
    public Object peek() throws QueueEmptyException {
        Object x;
        return x;
    }
 
    // dequeueAll()
    // sets this Queue to the empty state
    // pre: !isEmpty()
    // post: isEmpty()
    public void dequeueAll() throws QueueEmptyException {

    }

    //------------------------------------------------------------------------------------------------------------------------
    // Other Methods
    //------------------------------------------------------------------------------------------------------------------------
 
    // toString()
    // overrides Object's toString() method
    public String toString() {
        String a = "";
        return a;
    }

}