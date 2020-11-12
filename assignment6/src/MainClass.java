import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
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
    public static final String IN_FILENAME = "conticorrenti.json"; // file di default che viene provato ad aprire
    public static final int BUFFER_SIZE = 1024*1024; // spazio di allocazione del buffer
    public static final int EOS = -1; // end of stream

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
        * 0 argomenti: proverà ad utilizzare il file di default IN_FILENAME
        * 1 argomento: path del file json da analizzare
        * */
        if(args.length == 0) {
            System.out.println("Nessun file passato come parametro, proverò ad aprire il file " + IN_FILENAME);
            inputFile = new File(IN_FILENAME);
        } else if (args.length == 1) {
            inputFile = new File(args[0]);
        } else {
            System.out.println("Usage: java MainClass [ <complete json file path> ]");
            return;
        }

        if(!inputFile.canRead()) {
            System.out.println("Il file " + inputFile.getAbsolutePath() + " non esiste o non può essere aperto in lettura.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<ContoCorrente> contiCorrenti = null;

        // deserializzazione
        try {
            FileChannel inChannel = new FileInputStream(inputFile).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            StringBuilder tmp = new StringBuilder(); // stringa dinamica
            while(inChannel.read(buffer) != EOS) {
                buffer.flip();
                tmp.append(StandardCharsets.UTF_8.decode(buffer)); // necessario per decodificare il buffer
                buffer.clear();
            }
            contiCorrenti = objectMapper.readValue(tmp.toString(), new TypeReference<List<ContoCorrente>>(){});
            inChannel.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, SafeCounter> counters = getCounters();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        if(contiCorrenti != null) {
            for (ContoCorrente conto : contiCorrenti) {
                threadPool.submit(new Analizzatore(counters, conto));
            }
        }

        // termino threadpool
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
            System.out.format("Sono stati effetuati %d movimenti con causale %s\n", c.getValue(), key);
        }
    }

}
