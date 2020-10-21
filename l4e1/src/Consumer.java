/**
 * Created by alessiomatricardi on 21/10/2020
 */
public class Consumer implements Runnable {
    private boolean consumaPari;
    private Dropbox db;

    public Consumer(boolean consumaPari, Dropbox db) {
        this.consumaPari = consumaPari;
        this.db = db;
    }

    @Override
    public void run() {
        while (true) {
            int x = db.take(consumaPari);
            System.out.println("Consumer " + Thread.currentThread().getName() + ": ho consumato " + x);
        }
    }
}
