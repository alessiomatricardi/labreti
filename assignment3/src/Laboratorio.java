import java.security.InvalidParameterException;
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
        protected enum Stato { LIBERO, OCCUPATO };
        Stato stato;

        public Computer() {
            stato = Stato.LIBERO;
        }

        public Stato getStato() {
            return stato;
        }

        public void setStato() {
            stato = (this.getStato() == Stato.LIBERO ? Stato.OCCUPATO : Stato.LIBERO);
        }
    }

    private static final String nomeLab = "Laboratorio Marzotto";
    private static final int NUM_POSTAZIONI = 20;
    public static final int NO_AVAILABLE = -1;
    private Computer[] postazioni;
    private int numPostazioniLibere;
    private ReentrantLock lock;

    public Laboratorio () {
        postazioni = new Computer[NUM_POSTAZIONI];
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            postazioni[i] = new Computer();
        }
        numPostazioniLibere = NUM_POSTAZIONI;
        lock = new ReentrantLock();
    }

    public int getNUMPostazioniLibere() {
        int tmp;
        lock.lock();
        tmp = numPostazioniLibere;
        lock.unlock();
        return tmp;
    }

    public boolean isFull() {
        boolean guard;
        lock.lock();
        guard = numPostazioniLibere == 0;
        lock.unlock();
        return guard;
    }

    public boolean isEmpty() {
        boolean guard;
        lock.lock();
        guard = numPostazioniLibere == NUM_POSTAZIONI;
        lock.unlock();
        return guard;
    }

    public boolean isAvailable(int pos) throws InvalidParameterException {
        if (pos < 0 || pos >= NUM_POSTAZIONI) throw new InvalidParameterException();
        boolean guard;
        lock.lock();
        guard = postazioni[pos].getStato() == Computer.Stato.LIBERO;
        lock.unlock();
        return guard;
    }

    public int getFirstAvailable() {
        int first = NO_AVAILABLE;
        lock.lock();
        for (int i = 0; i < NUM_POSTAZIONI; i++) {
            if (postazioni[i].getStato() == Computer.Stato.LIBERO) {
                first = i;
                break;
            }
        }
        lock.unlock();
        return first;
    }
}
