public class Reader implements Runnable {
    private int index;
    private Counter counter;

    public Reader(int idx, Counter c) {
        index = idx;
        counter = c;
    }
    @Override
    public void run() {
        System.out.format("Reader %3d : Counter è %d\n", this.index, counter.get());
    }
}
