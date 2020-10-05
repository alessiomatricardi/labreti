import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;

public class MainClass {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("MainClass <num>");
            return;
        }
        double n = 0;
        try {
            n = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Impossibile recuperare il numero");
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ArrayList<Future<Double>> partial = new ArrayList<Future<Double>>();
        for (int i = 2; i <= 50; i++) {
            Power p = new Power(n, i);
            partial.add(threadPool.submit(p));
        }
        double result = 0;
        Iterator<Future<Double>> it = partial.iterator();
        while (it.hasNext()) {
            try {
                double x = it.next().get();
                result += x;
            }
            catch (CancellationException | InterruptedException | ExecutionException e) {
                System.out.println("Si è verificata un'eccezione");
                return;
            }
        }
        threadPool.shutdown();
        System.out.println("Il risultato è " + result);
    }
}
