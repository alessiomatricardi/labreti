/**
 * Created by alessiomatricardi on 12/11/2020
 */
public class SafeCounter {
    private long counter;

    public SafeCounter() {
        counter = 0;
    }

    public synchronized void increment() {
        counter++;
    }

    public synchronized long getCounter() {
        return counter;
    }
}
