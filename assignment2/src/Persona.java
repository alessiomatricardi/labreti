import java.util.concurrent.ThreadLocalRandom;

public class Persona implements Runnable {
    private static final int MIN_TEMPO = 1;
    private static final int MAX_TEMPO = 1000;
    private final int numero; // numero staccato all'ingresso

    public Persona(int numero) {
        this.numero = numero;
    }

    @Override
    public void run() {
        System.out.println("Persona " + this.numero + ": inizia operazione allo sportello " + Thread.currentThread().getId());
        int random = ThreadLocalRandom.current().nextInt(MIN_TEMPO,MAX_TEMPO + 1);
        try {
            Thread.sleep(random);
        }
        catch (InterruptedException e) {
            System.out.println("Operazione interrotta, l'operatore ha bisogno di un caffè... :)");
        }
        System.out.println("Persona " + this.numero + ": terminata operazione allo sportello " + Thread.currentThread().getId() +
                ", operazione completata in " + random + "msec");
    }
}
