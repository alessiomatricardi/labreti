import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteCounter extends Counter {
    private Lock writeLock;
    private Lock readLock;

    public ReadWriteCounter() {
        super();
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
    }

    @Override
    public void increment() {
        writeLock.lock();
        super.increment();
        writeLock.unlock();
    }

    @Override
    public int get() {
        readLock.lock();
        int value = super.get();
        readLock.unlock();
        return value;
    }
}
