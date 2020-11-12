import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            return causale.toString();
        }

        /* Helpful link for string to date conversion
         * https://stackoverflow.com/questions/4216745/java-string-to-date-conversion
         *
         */
        public void setData(String dataString) {
            String JsonPattern = "E MMM dd HH:mm:ss zzz yyyy";
            SimpleDateFormat formatter = new SimpleDateFormat(JsonPattern);
            try {
                data = formatter.parse(dataString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public void setCausale(String sCausale) {
            causale = Causale.valueOf(sCausale);
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setMovimenti(List<Movimento> movs) {
        movimenti = movs;
    }

    public void aggiungiMovimento(Movimento m) {
        this.movimenti.add(m);
    }
}
