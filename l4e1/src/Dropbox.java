/**
 * Dopbox modella un bounded buffer di dimensione 1
 * @author Samuel Fabrizi
 * @version 1.1
 */

class Dropbox {
    /**
     full è uguale a true se il buffer è pieno, false altrimenti
     */
    protected boolean full = false;
    /**
     * num valore del buffer (utile solo se il buffer è pieno)
     */
    protected int num;

    /**
     * Attende che il buffer contenga un numero, poi lo recupera e lo ritorna
     * @param e indica l'interesse a consumare un numero pari o dispari
     * 			se e == True il numero contenuto è pari, altrimenti è dispari
     * @return numero consumato
     */
    public int take(boolean e) {
        /* La seguente espressione equivale a:
         * if (e == true) {
         * 		s = "Pari"
         * }
         * else {
         * 		s = "Dispari
         * }
         */
        String s = e ? "Pari" : "Dispari";

        while (!full || e == (num % 2 != 0)) { //num non è quello cercato
            System.out.println("Attendi per: " + s);
            try {
                Thread.sleep((long) (Math.random()*100));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Thread.sleep((long) (Math.random()*1000));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println(s + " <-> " + num);
        full = false;
        return num;
    }

    /**
     * Attende che il buffer sia vuoto, poi inserisce n all'interno di esso
     * @param n intero da inserire nel buffer
     */
    public void put(int n) {
        while (full) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Producer ha inserito " + n);
        num = n;
        full = true;
    }
}