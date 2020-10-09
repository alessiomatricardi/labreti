/*
*
* Scrivere un programma in cui un contatore viene aggiornato da 20 scrittori e il suo valore letto e stampato da 20 lettori.
*
* Creare una Classe Counter che offre i metodi increment() e get() per incrementare e recuperare il valore di un contatore.
* Vedi esempio di struttura di una classe Counter (non-thread safe) in allegato
*
* Definire un task Writer che implementa Runnable e nel metodo run invoca il metodo increment di un oggetto Counter
*
* Definire un task Reader che implementa Runnable e nel metodo run invoca il metodo get di un oggetto Counter e lo stampa
*
* Definire una classe contenente il metodo main. Nel main viene creata un’istanza di Counter. Vengono quindi creati
* 20 oggetti di tipo Writer e 20 oggetti di tipo Reader (a cui viene passato il riferimento all’oggetto counter nel costruttore).
* I task vengono quindi assegnati ad un threadpool (inviare al pool prima i writer e poi i reader)
* (suggerimento: usare un CachedThreadPool).
*
* Estendere la classe Counter fornita usando un oggetto di tipo ReentrantLock per garantire l’accesso in mutua esclusione
* alle sezioni critiche.
*
* Estendere la classe Counter usando al posto di ReentrantLock delle Read/Write Lock e confrontare l’intervallo di tempo
* richiesto dal threadpool per completare i task in questo caso col caso precedente
* (usare System.currentTimeMillis() per recuperare l’ora corrente, potete prendere un primo timestamp prima del ciclo
* di creazione dei task e il secondo timestamp dopo la terminazione del threadpool).
*
* (opzionale) Sostituire il threadpool di tipo CachedThreadPool con un FixedThreadPool, al variare del numero di thread
* (es. 1 ,2, 4) verificare l’intervallo di tempo richiesto dal threadpool per completare i task
* */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainClass {
    static final int NUM_WRITERS = 20;
    static final int NUM_READERS = 20;

    public static void main(String[] args) {
        Reader[] readers = new Reader[NUM_READERS];
        Writer[] writers = new Writer[NUM_WRITERS];
        Counter c;
        ExecutorService threadPool;

        // implementazione NON thread safe
        System.out.println("Implementazione non thread-safe");

        c = new Counter();

        for (int i = 0; i < NUM_READERS; i++) {
            readers[i] = new Reader(i, c);
            writers[i] = new Writer(i, c);
        }

        threadPool = Executors.newCachedThreadPool();

        for (Writer w : writers) {
            threadPool.submit(w);
        }

        for (Reader r : readers) {
            threadPool.submit(r);
        }

        threadPool.shutdown();
        while(!threadPool.isTerminated()) {
            try {
                threadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // implementazione thread safe
        System.out.println("Implementazione thread-safe con ReentrantLock");

        c = new ReentrantCounter();

        for (int i = 0; i < NUM_READERS; i++) {
            readers[i] = new Reader(i, c);
            writers[i] = new Writer(i, c);
        }

        threadPool = Executors.newCachedThreadPool();

        long start1 = System.currentTimeMillis();

        for (Writer w : writers) {
            threadPool.submit(w);
        }

        for (Reader r : readers) {
            threadPool.submit(r);
        }

        threadPool.shutdown();
        while(!threadPool.isTerminated()) {
            try {
                threadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end1 = System.currentTimeMillis();

        // implementazione thread safe con Read/Write Lock
        System.out.println("Implementazione thread-safe con Read/Write Lock");

        c = new ReadWriteCounter();

        for (int i = 0; i < NUM_READERS; i++) {
            readers[i] = new Reader(i, c);
            writers[i] = new Writer(i, c);
        }

        threadPool = Executors.newCachedThreadPool();

        long start2 = System.currentTimeMillis();

        for (Writer w : writers) {
            threadPool.submit(w);
        }

        for (Reader r : readers) {
            threadPool.submit(r);
        }

        threadPool.shutdown();
        while(!threadPool.isTerminated()) {
            try {
                threadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end2 = System.currentTimeMillis();

        System.out.format("Tempo di esecuzione con ReentrantLock = %d ms\n", end1 - start1);
        System.out.format("Tempo di esecuzione con ReadWriteLock = %d ms\n", end2 - start2);
    }

}
