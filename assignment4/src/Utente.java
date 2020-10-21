import java.time.LocalDateTime;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public abstract class Utente implements Runnable {
    // Costanti
    private static final int MAX_K = 4;
    private static final int MAX_PERMANENZA_MS = 1000;
    private static final int MAX_INTERVALLO_MS = 1000;

    private final int idx; // indice univoco dell'utente
    private final int kRichieste; // Numero k di richieste, random da 1 a @MAX_K
    private LocalDateTime ultimaPrenotazione; // Data di ultima prenotazione dal tutor
    private final int intervalloAccessi; // Intervallo di richiesta dopo aver terminato di utilizzare una postazione

    private final PriorityBlockingQueue<Utente> coda; // coda di richieste al tutor
    protected final Laboratorio laboratorio;

    private boolean authorized; // autorizzato ad accedere?

    public Utente(int idx, PriorityBlockingQueue<Utente> queue, Laboratorio lab) {
        this.idx = idx;
        kRichieste = ThreadLocalRandom.current().nextInt(1, MAX_K + 1);
        ultimaPrenotazione = null;
        intervalloAccessi = ThreadLocalRandom.current().nextInt(1, MAX_INTERVALLO_MS + 1);
        authorized = false;
        coda = queue;
        laboratorio = lab;
    }

    public void run() {
        for (int i = 0; i < kRichieste; i++) {
            // Richiedi utilizzo della postazione
            this.vaiInCoda();

            System.out.format("%s %d: utilizzo il/i PC per la %d/%d volta\n", this.getClass().getSimpleName(), this.idx, i + 1, kRichieste);
            // Utilizza postazione
            this.utilizzaPC();

            // Notifica di averla finita di utilizzare
            this.notificaUscita();

            // Aspetta che scada l'intervallo prima di richiedere un nuovo accesso
            this.aspettaIntervallo();
        }
        System.out.format("%s %d: ho finito\n", this.getClass().getSimpleName(), this.idx);
    }

    private synchronized void vaiInCoda() {
        this.setUltimaPrenotazione();
        coda.add(this);

        while (!this.authorized) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        authorized = false;
    }

    private void utilizzaPC() {
        int time = ThreadLocalRandom.current().nextInt(0, MAX_PERMANENZA_MS + 1);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void notificaUscita(); // implementato nelle classi concrete

    private void aspettaIntervallo() {
        try {
            Thread.sleep(intervalloAccessi);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void autorizzaUtente() {
        authorized = true;
        this.notify();
    }

    public LocalDateTime getUltimaPrenotazione() {
        return this.ultimaPrenotazione;
    }

    private void setUltimaPrenotazione() {
        this.ultimaPrenotazione = LocalDateTime.now();
    }

    public int getIndex() {
        return idx;
    }
}
