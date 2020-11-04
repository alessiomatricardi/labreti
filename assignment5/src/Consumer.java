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
                String path = coda.dequeue();
                File dir = new File(path);
                File[] elements = dir.listFiles();
                synchronized (System.out) {
                    System.out.println("Consumer " + Thread.currentThread().getName() + ": File contenuti nella directory '"
                            + dir.getParent() +"/"+ dir.getName() + "' :");
                    if (elements != null) {
                        int count = 0;
                        for (File elem : elements) {
                            if (elem.isFile()) {
                                System.out.println(++count + ") " + elem.getName());
                            }
                        }
                    } else {
                        System.out.println("Nessun file presente");
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.format("Consumer %s: lavoro finito\n", Thread.currentThread().getName());
                return;
            }
        }
    }
}
