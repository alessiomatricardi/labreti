import java.util.Calendar;

public class DatePrinterThread extends Thread{

    public static void main(String[] args) {
        DatePrinterThread dpt = new DatePrinterThread();
        dpt.start();
        System.out.println(("THREAD: " + Thread.currentThread().getName()));
    }

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