public class MainClass {

    public static void main(String[] args) {
        double accuracy = 0;
        long msec = 1000;
        /* ricavo valori da input*/
        if(args.length != 2) {
            System.out.println("Il numero degli argomenti non sembra corretto");
            System.out.println("Sintassi:\njava <accuratezza> <millisecondi>");
            return;
        }
        try {
            accuracy = Double.parseDouble(args[0]);
            msec = Long.parseLong(args[1]);
        }
        catch (Exception e) {
            System.out.println("C'è stato un errore nel parsing dei dati in ingresso");
            System.out.println("Sintassi:\njava <accuratezza> <millisecondi>");
            return;
        }

        PiCalculator piCalc = new PiCalculator(accuracy);
        Thread t = new Thread(piCalc);
        t.start();

        // provo a joinare il thread calcolatore per msec millisecondi
        try {
            t.join(msec);
        }
        catch (InterruptedException e) {
            System.out.println("Thread main interrotto...");
        }

        // invio interruzione se thread non terminato
        if(t.isAlive()) {
            // interrompo il thread calcolatore
            t.interrupt();
        }
    }
}
