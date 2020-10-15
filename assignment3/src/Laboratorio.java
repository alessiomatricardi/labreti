import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public class Laboratorio {

    /**
     * Rappresenta un computer del laboratorio
     *
     * La dichiarazione è privata per non consentire a nessuno,
     * all'infuori del laboratorio, di accedervi.
     * */
    private static class Computer {
        protected enum Stato { LIBERO, OCCUPATO }
        Stato stato;

        public Computer() {
            stato = Stato.LIBERO;
        }

        public Stato getStato() {
            return stato;
        }

        public void setStato(Stato stato) throws UnchangedStateException {
            if (this.stato == stato) throw new UnchangedStateException();
            this.stato = stato;
        }

        // Definizione di una eccezione personalizzata
        private static class UnchangedStateException extends Exception {}
    }

    private static final String nomeLab = "Laboratorio Marzotto";
    public static final int NUM_POSTAZIONI = 20;
    public static final int NO_AVAILABLE = -1;

    private final Computer[] postazioni;
    private int numPostazioniLibere;
    private final ReentrantLock lock;
    private final Condition full;

    public Laboratorio () {
        postazioni = new Computer[NUM_POSTAZIONI];
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            postazioni[i] = new Computer();
        }
        numPostazioniLibere = NUM_POSTAZIONI;
        lock = new ReentrantLock();
        full = lock.newCondition();
    }

    /*
    * Dai l'accesso ad una specifica postazione
    */
    public boolean giveAccess(int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= NUM_POSTAZIONI) throw new IllegalArgumentException();
        lock.lock();
        boolean libera = postazioni[pos].getStato() == Computer.Stato.LIBERO;
        if (libera) {
            try {
                postazioni[pos].setStato(Computer.Stato.OCCUPATO);
            } catch (Computer.UnchangedStateException e) {
                e.printStackTrace();
            }
            this.numPostazioniLibere--;
        }
        lock.unlock();
        return libera;
    }

    /*
    * Dai l'accesso alla prima postazione libera
    * */
    public int giveAccess() {
        int primaLibera = NO_AVAILABLE;
        lock.lock();
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            if (postazioni[i].getStato() == Computer.Stato.LIBERO) {
                primaLibera = i;
                try {
                    postazioni[i].setStato(Computer.Stato.OCCUPATO);
                } catch (Computer.UnchangedStateException e) {
                    e.printStackTrace();
                }
                this.numPostazioniLibere--;
                break;
            }
        }
        lock.unlock();
        return primaLibera;
    }

    /*
     * Dai l'accesso a tutte le postazioni
     * */
    public boolean giveFullAccess() {
        lock.lock();
        if (numPostazioniLibere < NUM_POSTAZIONI) {
            lock.unlock();
            return false;
        }
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            try {
                postazioni[i].setStato(Computer.Stato.OCCUPATO);
            } catch (Computer.UnchangedStateException e) {
                e.printStackTrace();
            }
        }
        this.numPostazioniLibere = 0;
        lock.unlock();
        return true;
    }

    /*
    * Rilascia la postazione pos
    */
    public void release (int pos) throws IllegalArgumentException {
        if (pos < 0 || pos >= NUM_POSTAZIONI) throw new IllegalArgumentException();
        lock.lock();
        boolean occupata = postazioni[pos].getStato() == Computer.Stato.OCCUPATO;
        if (occupata) {
            try {
                postazioni[pos].setStato(Computer.Stato.LIBERO);
            } catch (Computer.UnchangedStateException e) {
                e.printStackTrace();
            }
            this.numPostazioniLibere++;

            full.signal();
        }
        lock.unlock();
    }

    /*
     * Rilascia tutte le postazioni
     */
    public void releaseAll() {
        lock.lock();
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            try {
                postazioni[i].setStato(Computer.Stato.LIBERO);
            } catch (Computer.UnchangedStateException e) {
                e.printStackTrace();
            }
        }
        this.numPostazioniLibere = NUM_POSTAZIONI;
        full.signal();

        lock.unlock();
    }

    /*
     * Attendi finchè tutte le postazioni sono occupate
     */
    public void waitUntilFull() {
        lock.lock();
        while(numPostazioniLibere == 0) {
            try {
                full.await();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.unlock();
    }
}
