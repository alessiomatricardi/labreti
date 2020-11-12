import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private LocalDateTime data;
        private Causale causale;

        public Movimento (LocalDateTime data, Causale causale) {
            this.data = data;
            this.causale = causale;
        }

        public Movimento() {}

        public String getData() {
            return this.data.format(formatter);
        }

        public String getCausale() {
            return causale.toString();
        }

        /* Helpful link for string to date conversion
         * https://stackoverflow.com/questions/4216745/java-string-to-date-conversion
         *
         */
        public void setData(String dataString) {
            try {
                data = LocalDateTime.parse(dataString, formatter);
            } catch (DateTimeParseException e) {
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

    public ContoCorrente() {}

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
