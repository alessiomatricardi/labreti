import java.util.NoSuchElementException;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Tutor implements Runnable {
    public static final int INITIAL_CAPACITY = 20;
    private PriorityBlockingQueue<Utente> coda;
    Laboratorio laboratorio;

    public Tutor (Laboratorio lab, PriorityBlockingQueue<Utente> queue) {
        coda = queue;
        laboratorio = lab;
    }

    @Override
    public void run() {
        Utente utente;
        while (true) {
            // check se sono stato interrotto (l'esecuzione sta terminando)
            if (Thread.currentThread().isInterrupted()) {
                System.out.print("Tutor: è ora di andare a dormire...\n");
                return;
            }

            // seleziono primo utente dalla lista
            try {
                utente = coda.element();
            }
            catch (NoSuchElementException e) {
                continue;
            }

            // autorizzalo
            autorizza(utente);
        }
    }

    public void autorizza (Utente utente) {
        if (utente instanceof Professore) {
            if (laboratorio.giveFullAccess()) { // se lab vuoto, gli do l'accesso
                coda.remove();
                utente.authorize();
                System.out.format("Tutor: Autorizzo Professore %d\n", utente.getIndex());

                // aspetto che professore finisca altrimenti faccio solo attesa attiva
                laboratorio.waitUntilFull();
            }
            return;
        }

        if (utente instanceof Tesista) {
            int postazioneRichiesta = ((Tesista)utente).getPostazioneRichiesta();
            try {
                if (laboratorio.giveAccess(postazioneRichiesta)) {
                    coda.remove();
                    utente.authorize();
                    System.out.format("Tutor: Autorizzo Tesista %d alla postazione %d\n", utente.getIndex(), ((Tesista) utente).getPostazioneRichiesta());
                }
            }
            catch (IllegalArgumentException e) { // WHATTT???
                e.printStackTrace();
            }
            return;
        }

        if (utente instanceof Studente) {
            int postazioneOttenuta;
            if ((postazioneOttenuta = laboratorio.giveAccess()) != Laboratorio.NO_AVAILABLE) {
                coda.remove();
                ((Studente)utente).setPostazioneOttenuta(postazioneOttenuta);
                utente.authorize();
                System.out.format("Tutor: Autorizzo Studente %d alla postazione %d\n", utente.getIndex(), postazioneOttenuta);
            }
        }
    }
}
