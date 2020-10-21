/**
 * Created by alessiomatricardi on 21/10/2020
 */
public class MonitorDropbox extends Dropbox {

    @Override
    public int take(boolean e) {
        String s = e ? "Pari" : "Dispari";

        synchronized (this) {
            while (!full || e == (num % 2 != 0)) { //num non è quello cercato
                System.out.println("Attendi per: " + s);
                try {
                    this.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            Thread.sleep((long) (Math.random()*1000));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println(s + " <-> " + num);

        synchronized (this) {
            full = false;
            //this.notify();
            this.notifyAll();
        }

        return num;
    }

    @Override
    public synchronized void put(int n) {
        while (full) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Producer ha inserito " + n);

        num = n;
        full = true;
        //this.notify();
        this.notifyAll();
    }
}
