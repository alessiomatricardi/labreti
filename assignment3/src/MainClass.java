import java.util.concurrent.*;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public class MainClass {

    /**
     * @param args (numero professori, numero tesisti, numero studenti)
     * */
    public static void main(String[] args) {
        // controllo parametri
        if (args.length != 3) {
            System.out.println("Uso: ./MainClass <professori> <tesisti> <studenti>");
            return;
        }
        int numProfessori, numTesisti, numStudenti;
        try {
            numProfessori = Integer.parseInt(args[0]);
            numTesisti = Integer.parseInt(args[1]);
            numStudenti = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Errore parsing dati in ingresso");
            return;
        }

        // istanzio laboratorio e coda di utenti
        Laboratorio laboratorioMarzotto = new Laboratorio();
        PriorityBlockingQueue<Utente> queue = new PriorityBlockingQueue<>(Tutor.INITIAL_CAPACITY, new UserComparator());

        Professore[] professori = new Professore[numProfessori];
        Tesista[] tesisti = new Tesista[numTesisti];
        Studente[] studenti = new Studente[numStudenti];
        Tutor tutor = new Tutor(laboratorioMarzotto, queue);

        // pool di thread per gli utenti e thread unico per il tutor
        ExecutorService userThreads = Executors.newCachedThreadPool();
        Thread tutorThread = new Thread(tutor);

        // istanze e esecuzione dei thread
        for (int i = 0; i < numProfessori; i++) {
            professori[i] = new Professore(i, queue, laboratorioMarzotto);
            userThreads.execute(professori[i]);
        }
        for (int i = 0; i < numTesisti; i++) {
            int postazione = ThreadLocalRandom.current().nextInt(0, Laboratorio.NUM_POSTAZIONI);
            tesisti[i] = new Tesista(i, queue, laboratorioMarzotto, postazione);
            userThreads.execute(tesisti[i]);
        }
        for (int i = 0; i < numStudenti; i++) {
            studenti[i] = new Studente(i, queue, laboratorioMarzotto);
            userThreads.execute(studenti[i]);
        }
        tutorThread.start();

        userThreads.shutdown();
        while (!userThreads.isTerminated()) {
            try {
                userThreads.awaitTermination(1000, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // interrompo il thread Tutor quando non ho più utenti
        tutorThread.interrupt();

        try {
            tutorThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("È tardi, il " + laboratorioMarzotto.getNomeLab() + " chiude.");
    }

}
