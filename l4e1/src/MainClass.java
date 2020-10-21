/*
 * Scrivere un programma in cui alcuni thread generano e consumano numeri interi da una risorsa
 * condivisa (chiamata Dropbox) che ha capacità 1.
 * 1. Nella classe Dropbox fornita in allegato il buffer di dimensione 1 è gestito tramite una variabile
 * intera num. La classe offre un metodo take per consumare il numero e svuotare il buffer e un
 * metodo put per inserire un nuovo valore se il buffer è vuoto.
 * 2. Definire un task Consumer il cui metodo costruttore prende in ingresso un valore booleano
 * (true per consumare valori pari e false per valori dispari) e il riferimento ad un’istanza di
 * Dropbox. Nel metodo run invoca il metodo take sull’istanza di Dropbox.
 * 3. Definire un task Producer il cui metodo costruttore prende in ingresso il riferimento ad
 * un’istanza di Dropbox. Nel metodo run genera un intero in modo random, nel range [0,100), e
 * invoca il metodo put sull’istanza di Dropbox.
 * 4. Definire una classe contenente il metodo main. Nel main viene creata un’istanza di Dropbox.
 * Vengono quindi creati 2 oggetti di tipo Consumer (uno true e uno false) e un oggetto di tipo
 * Producer, ciascuno eseguito da un thread distinto.
 * 5. Estendere la classe Dropbox (overriding dei metodi take e put) usando il costrutto del
 * monitor per gestire l’accesso di Consumer e Producer al buffer. Notare la differenza nell’uso
 * di notify vs notifyall
 */

/**
 * Created by alessiomatricardi on 21/10/2020
 */
public class MainClass {
    public static void main(String[] args) {
        //Dropbox db = new Dropbox();
        Dropbox db = new MonitorDropbox();
        Consumer c1 = new Consumer(true, db);
        Consumer c2 = new Consumer(false, db);
        Producer p = new Producer(db);

        Thread c1Thread = new Thread(c1);
        Thread c2Thread = new Thread(c2);
        Thread prodThread = new Thread(p);

        c1Thread.start();
        c2Thread.start();
        prodThread.start();

        try {
            c1Thread.join();
            c2Thread.join();
            prodThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
