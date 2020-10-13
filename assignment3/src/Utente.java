import java.time.LocalDateTime;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public abstract class Utente implements Runnable {
    private static final int MAX_K = 10;
    private static final int MAX_PERMANENZA_MS = 1000;
    private static final int MAX_INTERVALLO_MS = 1000;
    protected int kRichieste;
    private LocalDateTime ultimaPrenotazione;
    protected int intervalloAccessi;
    private boolean authorized;
    private PriorityBlockingQueue<Utente> coda;

    public Utente () {
        kRichieste = ThreadLocalRandom.current().nextInt(1, MAX_K + 1);
        ultimaPrenotazione = null;
        intervalloAccessi = ThreadLocalRandom.current().nextInt(1, MAX_INTERVALLO_MS + 1);
        authorized = false;
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

    protected void aspettaIntervallo () {
        // reset authorized
        // lock
        this.setAuthorized(false);
        // unlock

        try {
            Thread.sleep(intervalloAccessi);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getkRichieste () {
        return kRichieste;
    }

    public LocalDateTime getUltimaPrenotazione() {
        return this.ultimaPrenotazione;
    }

    public void setUltimaPrenotazione() {
        this.ultimaPrenotazione = LocalDateTime.now();
    }

    public void setAuthorized(boolean hasAccess) {
        this.authorized = hasAccess;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void vaiInCoda() {
        // lock qualcosa
        coda.offer(this);
        while (!this.isAuthorized()) {
            // dormi
        }
        // unlocka
    }
}
