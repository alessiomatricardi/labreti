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

        Professore[] professori = new Professore[numProfessori];
        Tesista[] tesisti = new Tesista[numTesisti];
        Studente[] studenti = new Studente[numStudenti];
        Tutor tutor;

    }

}
