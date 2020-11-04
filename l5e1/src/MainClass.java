import java.io.*;

/**
 * Created by alessiomatricardi on 04/11/2020
 */

/*
 * Scrivere un programma Java che, a partire dal percorso di una directory (es. "/path/to/dir/"), recupera il contenuto della directory e delle eventuali sottodirectory.
 *
 * Il programma scrive in un file di nome “directories” il nome delle directory che incontra e nel file “files” il nome dei file.
 */
public class MainClass {
    public static final String FILES_FILENAME = "./files";
    public static final String DIRECTORIES_FILENAME = "./directories";

    /**
     * @param args array contenente 1 solo elemento (il path della directory da aprire)
     * */
    public static void main (String[] args) {
        // check numero argomenti
        if(args.length != 1) {
            System.out.format("Errore! Numero di parametri passato: %d, numero di parametri atteso: 1\n", args.length);
            return;
        }
        // path directory principale
        String mainPath = args[0];
        File mainFile = new File(mainPath);
        if(!mainFile.isDirectory()) {
            System.out.format("Il path '%s' non corrisponde ad una directory oppure essa non è esistente\n", mainPath);
            return;
        }

        // apro files in cui scriverò
        File files = new File(FILES_FILENAME);
        File directories = new File(DIRECTORIES_FILENAME);

        try {
            if(files.createNewFile()) {
                System.out.format("File '%s' creato\n", FILES_FILENAME);
            }
            if (directories.createNewFile()) {
                System.out.format("File '%s' creato\n", DIRECTORIES_FILENAME);
            }
            FileWriter filesOut = new FileWriter(files);
            FileWriter directoriesOut = new FileWriter(directories);

            analyze_dir(mainFile, filesOut, directoriesOut);

            filesOut.close();
            directoriesOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dir file che rappresenta la directory
     * @param filesOut dove scrivere i nomi dei files
     * @param dirOut dove scrivere i nomi delle directories
     */
    public static void analyze_dir (File dir, FileWriter filesOut, FileWriter dirOut) {
        File[] elements = dir.listFiles();

        try {
            for (File element : elements) {
                if(element.isDirectory()) {
                    // write dentro file directories
                    dirOut.write(element.getName()+'\n');
                    analyze_dir(element.getAbsoluteFile(), filesOut, dirOut);
                } else {
                    // write dentro file files
                    filesOut.write(element.getName()+'\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
