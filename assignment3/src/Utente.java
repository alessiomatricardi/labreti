import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public abstract class Utente implements Runnable {
    private static final int MAX_K = 10;
    private static final int MAX_PERMANENZA_MS = 1000;
    private int kRichieste;

    public Utente () {
        kRichieste = ThreadLocalRandom.current().nextInt(1, MAX_K + 1);
    }

    protected void utilizzaPC () {
        int time = ThreadLocalRandom.current().nextInt(0, MAX_PERMANENZA_MS + 1);
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getkRichieste () {
        return kRichieste;
    }

}
