import java.util.concurrent.*;

/**
 * Created by alessiomatricardi on 11/10/2020
 */
public class MainClass {

    /**
     * @param args (numero professori, numero tesisti, numero studenti)
     * */
    public static void main(String[] args) {
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

        Laboratorio laboratorioMarzotto = new Laboratorio();
        PriorityBlockingQueue<Utente> queue = new PriorityBlockingQueue<>(Tutor.INITIAL_CAPACITY, new UserComparator());

        Professore[] professori = new Professore[numProfessori];
        Tesista[] tesisti = new Tesista[numTesisti];
        Studente[] studenti = new Studente[numStudenti];
        Tutor tutor = new Tutor(laboratorioMarzotto, queue);

        ExecutorService userThreads = Executors.newCachedThreadPool();
        Thread tutorThread = new Thread(tutor);

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

        tutorThread.interrupt();

        try {
            tutorThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
