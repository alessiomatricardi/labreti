import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Studente extends Utente {
    private int postazioneOttenuta;

    public Studente(int idx, PriorityBlockingQueue<Utente> queue, Laboratorio lab) {
        super(idx, queue, lab);
        postazioneOttenuta = Laboratorio.NO_AVAILABLE;
    }

    public void setPostazioneOttenuta(int postazioneOttenuta) {
        this.postazioneOttenuta = postazioneOttenuta;
    }

    @Override
    protected void notificaUscita() {
        try {
            laboratorio.release(postazioneOttenuta);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
