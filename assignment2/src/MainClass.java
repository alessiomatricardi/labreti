import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {
    public static void main(String[] args) {
        /* controlli sui dati dal terminale in ingresso */
        if (args.length != 2) {
            System.out.println("java ./MainClass <numClientiTotali> <capacita' sala piu' interna>");
            return;
        }

        int numClienti = 0;
        int kSala = 0;

        try {
            numClienti = Integer.parseInt(args[0]);
            kSala = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("Errore nel formato degli input");
            return;
        }
        if (numClienti <= 0 || kSala <= 0) {
           System.out.println("Errore: deve esserci almeno un cliente e la sala deve avere capacità > 0");
           return;
        }
        /* operazioni */
        UfficioPostale ufficio = new UfficioPostale(kSala);
        BlockingQueue<Persona> codaEsterna = new LinkedBlockingQueue<Persona>();

        for(int i = 1; i <= numClienti; i++) {
            Persona p = new Persona(i);
            codaEsterna.add(p);
        }

        while (numClienti > 0) { // attesa attiva ma necessaria
            try {
                Persona p = codaEsterna.element();
                if (ufficio.serviPersona(p)) {
                    codaEsterna.remove();
                    numClienti--;
                }
            }
            catch (NoSuchElementException e) {
                System.out.println("Nessuna persona da dover servire...");
                break;
            }
        }

        ufficio.chiudiUfficio();
    }
}
