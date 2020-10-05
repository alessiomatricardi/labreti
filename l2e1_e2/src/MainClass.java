import java.util.concurrent.*;

public class MainClass {
    public static void main(String[] args) {
        int msec = 50;
        Sala sala = new Sala();
        for(int i=0; i < 50; i++) {
            Viaggiatore v = new Viaggiatore(i);
            try {
                sala.serviViaggiatore(v);
            }
            catch (RejectedExecutionException e){
                System.out.println("Viaggiatore " + i + ": sala esaurita");
            }
            try {
                Thread.sleep(msec);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        sala.chiudiSala();

        System.out.println("La sala ha servito " + sala.getViaggiatoriServiti() + " viaggiatori");
    }
}
