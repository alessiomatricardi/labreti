public class Writer implements Runnable {
    private int index;
    private Counter counter;

    public Writer (int idx, Counter c) {
        index = idx;
        counter = c;
    }

    @Override
    public void run() {
        counter.increment();
    }
}
