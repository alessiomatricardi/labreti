import java.util.Calendar;

public class DatePrinter {
    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        long timeSleep = 2000;
        while(true) {
            System.out.println("DATE: "+ Calendar.getInstance().getTime().toString() + "\nTHREAD: " + t.getName());
            try {
                Thread.sleep(timeSleep);
            }
            catch (InterruptedException e) {
                System.out.println("thread interrotto...");
            }
        }
        // non verrà mai eseguito
        // System.out.println("THREAD: " + t.getName());
    }
}
