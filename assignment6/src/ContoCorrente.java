import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alessiomatricardi on 11/11/2020
 */
public class ContoCorrente {

    public enum Causale {
        BONIFICO,
        ACCREDITO,
        BOLLETTINO,
        F24,
        PAGOBANCOMAT
    }

    public static class Movimento {

        private Date data;
        private Causale causale;

        public Movimento (Date data, Causale causale) {
            this.data = data;
            this.causale = causale;
        }

        public String getData() {
            return this.data.toString();
        }

        public String getCausale() {
            switch (this.causale) {
                case BONIFICO :
                    return "Bonifico";
                case ACCREDITO:
                    return "Accredito";
                case BOLLETTINO:
                    return "Bollettino";
                case F24:
                    return "F24";
                case PAGOBANCOMAT:
                    return "PagoBancomat";
            }
            return "N/D";
        }

    }

    private String nome;
    private String cognome;
    private List<Movimento> movimenti;

    public ContoCorrente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
        this.movimenti = new ArrayList<Movimento>();
    }

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public List<Movimento> getMovimenti() {
        return this.movimenti;
    }

    public void aggiungiMovimento(Movimento m) {
        this.movimenti.add(m);
    }
}
