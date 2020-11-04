import java.io.File;

/**
 * Created by alessiomatricardi on 04/11/2020
 */
public class Producer implements Runnable {
    private String path;
    private ConcurrentQueue coda;

    public Producer(String path, ConcurrentQueue coda) {
        this.path = path;
        this.coda = coda;
    }

    @Override
    public void run() {
        File mainPath = new File(path);
        if(!mainPath.isDirectory()) {
            System.out.format("Producer: Il path '%s non è una directory o non è esistente\n", path);
            coda.jobIsDone(); // dico a tutti i consumatori che ho finito
            return;
        }
        recursiveDir(mainPath); // funzione ricorsiva che analizza tutto l'albero della directory
        coda.jobIsDone();
        System.out.format("Producer: Lavoro finito\n");
    }

    public void recursiveDir (File dir) {
        coda.enqueue(dir.getAbsolutePath());
        File[] elements = dir.listFiles();
        if (elements != null) {
            for (File element : elements) {
                if (element.isDirectory()) {
                    recursiveDir(element.getAbsoluteFile());
                }
            }
        }
    }
}
