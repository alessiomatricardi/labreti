import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 21/10/2020
 */
public class Producer implements Runnable {
    private Dropbox db;

    public Producer(Dropbox db) {
        this.db = db;
    }

    @Override
    public void run() {
        while (true) {
            int x = ThreadLocalRandom.current().nextInt(0, 100);
            db.put(x);
            System.out.println("Producer " + Thread.currentThread().getName() + ": ho prodotto " + x);
        }
    }
}
