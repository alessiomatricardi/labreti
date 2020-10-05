import java.util.concurrent.Callable;

public class Power implements Callable<Double> {
    private double num;
    private int exp;

    public Power (double n, int exp) {
        this.num = n;
        this.exp = exp;
    }

    @Override
    public Double call() throws Exception {
        System.out.println("Esecuzione " + num + "^" + exp + " in " + Thread.currentThread().getName());
        return Math.pow(num, exp);
    }
}
