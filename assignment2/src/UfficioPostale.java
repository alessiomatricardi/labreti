import java.util.concurrent.*;

public class UfficioPostale {
    private final static int numSportelli = 4;
    private final static int timeout = 200;
    private ExecutorService threadPool;

    /**
     * @param k capacita' sala piu' interna
     *
     * @throws IllegalArgumentException se k < 0
     *
     * */
    public UfficioPostale(int k) throws IllegalArgumentException {
        if (k < 0) throw new IllegalArgumentException();
        threadPool = new ThreadPoolExecutor(numSportelli,
                numSportelli,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(k));
    }

    /**
     * @param p Persona da servire
     *
     * @return false se gli sportelli sono occupati e la sala interna e' piena, true altrimenti
     *
     * */
    public boolean serviPersona(Persona p) {
        try {
            threadPool.execute(p);
        }
        catch (RejectedExecutionException e) {
            return false;
        }
        return true;
    }

    public void chiudiUfficio() {
        threadPool.shutdown();

        while (!threadPool.isTerminated()) {
            try {
                threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                System.out.println("Shutdown interrotto");
            }
        }
    }
}
