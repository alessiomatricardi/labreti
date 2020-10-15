import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Tesista extends Utente {
    private final int postazioneRichiesta;

    public Tesista(int idx, PriorityBlockingQueue<Utente> queue, Laboratorio lab, int postazione) {
        super(idx, queue, lab);
        this.postazioneRichiesta = postazione;
    }

    public int getPostazioneRichiesta() {
        return postazioneRichiesta;
    }

    @Override
    protected void notificaUscita() {
        try {
            laboratorio.release(postazioneRichiesta);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
