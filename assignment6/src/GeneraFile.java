import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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

    public static final int NUM_CORRENTISTI = 100;
    public static final int MIN_MOVIMENTI = 50;
    public static final int MAX_MOVIMENTI = 600;
    public static final long TWO_YEARS_AGO_MSEC = 63113852000L;
    public static final String outFile = "conticorrenti.json";

    public static String getRandomName() {
        int pos = ThreadLocalRandom.current().nextInt(0, nomi.length);
        return nomi[pos];
    }

    public static String getRandomSurname() {
        int pos = ThreadLocalRandom.current().nextInt(0, cognomi.length);
        return cognomi[pos];
    }

    public static Date getRandomDate() {
        long oggi = new Date().getTime(); // esattamente questo momento
        long prima = oggi - TWO_YEARS_AGO_MSEC; // 2 anni fa
        long randomDay = ThreadLocalRandom.current().nextLong(prima, oggi);
        return new Date(randomDay);
    }

    public static ContoCorrente.Causale getRandomCausal() {
        ContoCorrente.Causale[] causali = ContoCorrente.Causale.values();
        int pos = ThreadLocalRandom.current().nextInt(0, causali.length);
        return causali[pos];
    }

    public static void main(String[] args) {
        ContoCorrente[] contiCorrenti = new ContoCorrente[NUM_CORRENTISTI];
        for (int i = 0; i < NUM_CORRENTISTI; i++) {
            contiCorrenti[i] = new ContoCorrente(getRandomName(), getRandomSurname());

            int num_movimenti = ThreadLocalRandom.current().nextInt(MIN_MOVIMENTI, MAX_MOVIMENTI + 1);

            Date data;
            ContoCorrente.Causale causale;
            ContoCorrente.Movimento m;
            while (num_movimenti-- > 0) {
                data = getRandomDate();
                causale = getRandomCausal();
                m = new ContoCorrente.Movimento(data, causale);
                contiCorrenti[i].aggiungiMovimento(m);
            }
        }

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