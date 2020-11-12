import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 12/11/2020
 */
public class GeneraFile {
    public static final String[] nomi = {"Alessio", "Francesco", "Stefano", "Giovanni", "Giulia",
            "Samuele", "Davide", "Francesca", "Benedetta", "Roberto", "Marco", "Mario", "Bruno", "Giuseppe"};
    public static final String[] cognomi = {"Rossi", "Bianchi", "Verdi", "Ferrari", "Russo", "Romano",
            "Gallo", "Costa", "Fontana", "Ferraro", "Caruso", "Matricardi", "Galli", "Conte", "Barbieri", "Piccoli", "Nicolo'"};

    public static final int NUM_CORRENTISTI = 100; // numero di correntisti da generare
    public static final int MIN_MOVIMENTI = 50; // numero minimo di movimenti di ogni correntista
    public static final int MAX_MOVIMENTI = 600; // numero massimo di movimenti di ogni correntista
    public static final long TWO_YEARS_AGO_SEC = 63115200L; // 2 anni in secondi
    public static final String outFile = "conticorrenti.json"; // nome del file da generare

    // restituisce un nome random dall'array nomi
    public static String getRandomName() {
        int pos = ThreadLocalRandom.current().nextInt(0, nomi.length);
        return nomi[pos];
    }

    // restituisce un cognome random dall'array cognomi
    public static String getRandomSurname() {
        int pos = ThreadLocalRandom.current().nextInt(0, cognomi.length);
        return cognomi[pos];
    }

    // restituisce una data random nell'intervallo (2 anni fa, ora)
    public static LocalDateTime getRandomDate() {
        long oggi = new Date().getTime() / 1000; // esattamente questo momento
        long prima = oggi - TWO_YEARS_AGO_SEC; // 2 anni fa
        long randomSecond = ThreadLocalRandom.current().nextLong(prima, oggi);
        return LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.UTC);
    }

    // restituisce una causale random
    public static ContoCorrente.Causale getRandomCausal() {
        ContoCorrente.Causale[] causali = ContoCorrente.Causale.values();
        int pos = ThreadLocalRandom.current().nextInt(0, causali.length);
        return causali[pos];
    }

    public static void main(String[] args) {
        ContoCorrente[] contiCorrenti = new ContoCorrente[NUM_CORRENTISTI];
        LocalDateTime data;
        ContoCorrente.Causale causale;

        for (int i = 0; i < NUM_CORRENTISTI; i++) {
            contiCorrenti[i] = new ContoCorrente(getRandomName(), getRandomSurname());

            int num_movimenti = ThreadLocalRandom.current().nextInt(MIN_MOVIMENTI, MAX_MOVIMENTI + 1);

            while (num_movimenti-- > 0) {
                data = getRandomDate();
                causale = getRandomCausal();
                contiCorrenti[i].aggiungiMovimento(new ContoCorrente.Movimento(data, causale));
            }
        }

        // serializzazione
        // Utilizzo libreria jackson per creazione JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(outFile);
            file.createNewFile();

            System.out.println("Scrivo su file JSON");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, contiCorrenti);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File JSON generato correttamente");
    }
}
