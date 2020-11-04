import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by alessiomatricardi on 04/11/2020
 */
public class ConcurrentQueue {
    private LinkedList<String> coda;
    private boolean producerHasFinished;

    public ConcurrentQueue() {
        coda = new LinkedList<String>();
        producerHasFinished = false;
    }

    public synchronized void enqueue (String elem) {
        coda.addLast(elem);
        this.notifyAll();
    }

    public synchronized String dequeue () throws NoSuchElementException {
        while(coda.size() == 0 && !producerHasFinished) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            return coda.removeFirst();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException();
        }
    }

    public synchronized void jobIsDone() {
        producerHasFinished = true;
        this.notifyAll();
    }
}
