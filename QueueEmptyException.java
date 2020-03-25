//============================================================================================================================
// QueueEmptyException.java
// This program will throw an Exception if the Queue is Empty.
// Tristan Clark
//============================================================================================================================

public class QueueEmptyException extends RuntimeException{
    public QueueEmptyException(String msg) {
        System.err.println(msg);
    }
}