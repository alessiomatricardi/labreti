import java.util.concurrent.locks.ReentrantLock;

public class ReentrantCounter extends Counter {
    private ReentrantLock lock;

    public ReentrantCounter() {
        super();
        lock = new ReentrantLock();
    }

    @Override
    public void increment() {
        lock.lock();
        super.increment();
        lock.unlock();
    }

    @Override
    public int get() {
        lock.lock();
        int value = super.get();
        lock.unlock();
        return value;
    }
}
