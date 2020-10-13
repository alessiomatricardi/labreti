/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Studente extends Utente {
    private int postazioneOttenuta;

    public Studente() {
        super();
        postazioneOttenuta = Laboratorio.NO_AVAILABLE;
    }

    @Override
    public void run() {
        for (int i = 0; i < kRichieste; i++) {
            // Richiedi utilizzo della postazione
            this.vaiInCoda();

            // Utilizza postazione
            this.utilizzaPC();

            // Notifica di averla finita di utilizzare

            // Aspetta che scada l'intervallo prima di richiedere un nuovo accesso
            this.aspettaIntervallo();
        }
    }

    public int getPostazioneOttenuta() {
        return postazioneOttenuta;
    }

    public void setPostazioneOttenuta(int postazioneOttenuta) {
        this.postazioneOttenuta = postazioneOttenuta;
    }
}
