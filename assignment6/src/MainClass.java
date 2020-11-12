import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by alessiomatricardi on 11/11/2020
 */
public class MainClass {
    public static final String defaultInFile = "conticorrenti.json"; // file di default che viene provato ad aprire

    /*
    * restituisce una struttura dati Map (TreeMap)
    * dove l'insieme di chiavi K è l'insieme delle possibili causali (in forma di stringa)
    * mentre Map[k] = contatore thread-safe, utilizzato per contare le occorrenze della causale k
    * */
    public static Map<String, SafeCounter> getCounters() {
        Map<String, SafeCounter> map = new TreeMap<String, SafeCounter>();
        for (ContoCorrente.Causale causale : ContoCorrente.Causale.values()) {
            map.put(causale.name(), new SafeCounter());
        }
        return map;
    }

    public static void main(String[] args) {
        File inputFile;

        /*
        * il programma accetta 0 o 1 argomento
        * 0 argomenti: proverà ad utilizzare il file di default dafaultInFile
        * 1 argomento: path del file json da analizzare
        * */
        if(args.length == 0) {
            System.out.println("Nessun file passato come parametro, proverò ad aprire il file " + defaultInFile);
            inputFile = new File(defaultInFile);
        } else if (args.length == 1) {
            inputFile = new File(args[0]);
        } else {
            System.out.println("Usage: java MainClass [ <complete json file path> ]");
            return;
        }

        if(!inputFile.canRead()) {
            System.out.println("Il file " + inputFile.getAbsolutePath() + " non esiste o non può essere letto.");
            return;
        }

        List<ContoCorrente> contiCorrenti;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, SafeCounter> counters = getCounters();
        ExecutorService threadPool = Executors.newCachedThreadPool();

        // deserializzazione
        try {
            contiCorrenti = objectMapper.readValue(inputFile, new TypeReference<List<ContoCorrente>>(){});
            for (ContoCorrente conto : contiCorrenti) {
                threadPool.submit(new Analizzatore(counters, conto));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
        while(!threadPool.isTerminated()) {
            try {
                threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // stampo risultato
        for(String key: counters.keySet()) {
            SafeCounter c = counters.get(key);
            System.out.format("Sono stati fatti %d movimenti con causale %s\n", c.getValue(), key);
        }
    }

}
