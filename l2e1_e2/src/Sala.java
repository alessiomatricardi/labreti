import java.util.concurrent.*;

public class Sala {
    private static final int numEmettitrici = 5;
    private static final int maxCoda = 10;
    ExecutorService threadPool;
    int viaggiatoriServiti;

    public Sala () {
        threadPool = new ThreadPoolExecutor(numEmettitrici,
                numEmettitrici,
                0L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(maxCoda));
        viaggiatoriServiti = 0;
    }

    public void serviViaggiatore(Viaggiatore v) throws RejectedExecutionException {
        try {
            threadPool.execute(v);
        }
        catch (RejectedExecutionException e) {
            throw new RejectedExecutionException();
        }
        viaggiatoriServiti ++;
    }

    public int getViaggiatoriServiti() {
        return viaggiatoriServiti;
    }

    public void chiudiSala() {
        int timeout = 1000;
        this.threadPool.shutdown();
        while(!this.threadPool.isTerminated()) {
            try {
                this.threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
