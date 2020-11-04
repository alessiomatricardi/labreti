import java.io.File;

/**
 * Created by alessiomatricardi on 04/11/2020
 */
public class MainClass {
    public static final int K = 10;

    /**
     * @param args array contenente 1 solo elemento (il path della directory da aprire)
     * */
    public static void main (String[] args) {
        // check numero argomenti
        if(args.length != 1) {
            System.out.format("Errore! Numero di parametri passato: %d, numero di parametri atteso: 1\n", args.length);
            System.out.format("Usage: java MainClass <path>\n");
            return;
        }
        // path directory principale
        String mainPath = args[0];
        File mainFile = new File(mainPath);
        if(!mainFile.isDirectory()) {
            System.out.format("Il path '%s' non corrisponde ad una directory oppure essa non è esistente\n", mainPath);
            return;
        }

        ConcurrentQueue coda = new ConcurrentQueue();

        Consumer[] consumers = new Consumer[K];
        for (int i = 0; i < K; i++) {
            consumers[i] = new Consumer(coda);
        }
        Producer producer = new Producer(mainPath, coda);

        Thread pThread = new Thread(producer);
        Thread[] cThread = new Thread[K];
        pThread.start();
        for (int i = 0; i < K; i++) {
            cThread[i] = new Thread(consumers[i]);
            cThread[i].start();
        }

        try {
            pThread.join();
            for (int i = 0; i < K; i++) {
                cThread[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
