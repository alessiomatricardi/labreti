import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by alessiomatricardi on 04/11/2020
 */

/*
* struttura di una coda di stringhe concorrente
* */
public class ConcurrentQueue {
    private LinkedList<String> coda; // struttura che mantiene dati
    private boolean producerHasFinished; // il produttore ha finito di produrre?

    public ConcurrentQueue() {
        coda = new LinkedList<>();
        producerHasFinished = false;
    }

    /**
     * @param elem stringa da inserire nella coda
     *
     * inserisce elem in coda e notifica tutti i consumatori
     */
    public synchronized void enqueue (String elem) {
        coda.addLast(elem);
        this.notifyAll();
    }

    /**
     * ritorna il primo elemento della coda
     *
     * @return il primo elemento della coda
     * @throws NoSuchElementException se la coda è vuota
     */
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

    /**
     * informa i consumatori che non verranno più immessi elementi nella coda
     */
    public synchronized void jobIsDone() {
        producerHasFinished = true;
        this.notifyAll();
    }
}
