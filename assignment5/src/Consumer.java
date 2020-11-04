import java.io.File;
import java.util.NoSuchElementException;

/**
 * Created by alessiomatricardi on 04/11/2020
 */
public class Consumer implements Runnable {
    private ConcurrentQueue coda;

    public Consumer(ConcurrentQueue coda) {
        this.coda = coda;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String path = coda.dequeue(); // estraggo dalla coda
                File dir = new File(path);
                File[] elements = dir.listFiles();
                // utilizzato affinchè ogni consumatore possa stampare tutti i file della sua directory senza essere interrotto
                synchronized (System.out) {
                    System.out.println("Consumer " + Thread.currentThread().getName() + ": File contenuti nella directory '"
                            + dir.getAbsolutePath() + "' :");
                    if (elements != null) {
                        int count = 0;
                        for (File elem : elements) {
                            if (elem.isFile()) {
                                System.out.println(++count + ") " + elem.getName());
                            }
                        }
                        if (count == 0) {
                            System.out.println("Nessun file presente");
                        }
                    } else {
                        System.out.println("Impossibile recuperare informazioni dalla directory");
                    }
                }
            } catch (NoSuchElementException e) { // il produttore non produrrà più nulla
                System.out.format("Consumer %s: lavoro finito\n", Thread.currentThread().getName());
                return;
            }
        }
    }
}
