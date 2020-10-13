import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Tutor implements Runnable {
    private static final int INITIAL_CAPACITY = 20;
    private PriorityBlockingQueue<Utente> coda;
    Laboratorio laboratorio;

    public Tutor (Laboratorio lab) {
        coda = new PriorityBlockingQueue<>(INITIAL_CAPACITY, new UserComparator());
        laboratorio = lab;
    }

    @Override
    public void run() {
        while (true) {
            // check se sono stato interrotto (l'esecuzione sta terminando)
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            // seleziono primo utente dalla lista
            Utente utente = coda.peek();
            if (utente == null) {
                continue;
            }
            if (utente instanceof Professore) {
                if (laboratorio.isEmpty()) { // se lab vuoto, gli do l'accesso
                    coda.poll(); // lo rimuovo dalla coda
                    this.giveAccess(utente);
                }
            } else if (utente instanceof Tesista) {
                int postazioneRichiesta = ((Tesista)utente).getPostazioneRichiesta();
                try {
                    if (laboratorio.isAvailable(postazioneRichiesta)) {
                        coda.poll();
                        this.giveAccess(utente);
                    }
                }
                catch (IllegalArgumentException e) { // WHATTT???/
                    e.printStackTrace();
                }
            } else {
                int postazioneOttenuta = laboratorio.getFirstAvailable();
                if (postazioneOttenuta != Laboratorio.NO_AVAILABLE) {
                    coda.poll();
                    ((Studente)utente).setPostazioneOttenuta(postazioneOttenuta);
                    this.giveAccess(utente);
                }
            }
        }
    }

    public void giveAccess (Utente utente) {
        utente.setAuthorized(true);
        // attendi qualcosa MA COSAAAA???
    }
}
