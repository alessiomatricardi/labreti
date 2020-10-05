import java.util.Calendar;

public class DatePrinterRunnable implements Runnable{

    public static void main(String[] args) {
        DatePrinterRunnable dpr = new DatePrinterRunnable();
        Thread thread = new Thread(dpr);
        thread.start();
        System.out.println(("THREAD: " + Thread.currentThread().getName()));
    }

    @Override
    public void run() {
        long timeSleep = 2000;
        while(true) {
            System.out.println("DATE: " + Calendar.getInstance().getTime());
            System.out.println(("THREAD: " + Thread.currentThread().getName()));
            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto...");
            }
        }
    }
}
