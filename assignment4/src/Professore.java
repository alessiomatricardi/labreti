import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Professore extends Utente {

    public Professore(int idx, PriorityBlockingQueue<Utente> queue, Laboratorio lab) {
        super(idx, queue, lab);
    }

    @Override
    protected void notificaUscita() {
        laboratorio.releaseAll();
    }
}
