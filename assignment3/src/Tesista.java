/**
 * Created by alessiomatricardi on 09/10/2020
 */
public class Tesista extends Utente {
    private int postazioneRichiesta;

    public Tesista(int postazione) {
        super();
        this.postazioneRichiesta = postazione;
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

    public int getPostazioneRichiesta() {
        return postazioneRichiesta;
    }

    public void setPostazioneRichiesta(int postazioneRichiesta) {
        this.postazioneRichiesta = postazioneRichiesta;
    }
}
