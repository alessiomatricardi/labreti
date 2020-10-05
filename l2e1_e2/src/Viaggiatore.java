import java.util.concurrent.ThreadLocalRandom;

public class Viaggiatore implements Runnable {
    private int id;

    public Viaggiatore(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Viaggiatore " + this.id + ": sto acquistando un biglietto");
        int time = ThreadLocalRandom.current().nextInt(0, 1001);
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Viaggiatore " + this.id + ": ho acquistato il biglietto");
    }
}
