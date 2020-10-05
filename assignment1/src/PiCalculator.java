public class PiCalculator implements Runnable {
    double accuracy;

    /**
    * @param accuracy reale positivo che indica la differenza massima tra MATH.PI e il calcolo
    * @throws IllegalArgumentException se accuracy è negativo
    */
    public PiCalculator(double accuracy) throws IllegalArgumentException {
        if(accuracy < 0) throw new IllegalArgumentException();
        this.accuracy = accuracy;
    }

    @Override
    public void run() {
        double myPi = 0;
        int i = 0;
        while(true) {
            if(Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName()+ ": ricevuta interruzione");
                return;
            }
            myPi += leibniz(i++);
            System.out.println("My Pi = "+myPi);
            System.out.println("Real Pi = "+Math.PI);
            double diff = Math.abs(myPi - Math.PI);
            System.out.println("Differisce da Math.PI di "+diff+"\n");
            if(diff < this.accuracy) {
                System.out.println(Thread.currentThread().getName()+ ": accuratezza voluta raggiunta dopo "+ i + " iterazioni");
                return;
            }
        }
    }

    /**
     * restituisce l'i-esimo elemento della serie di Gregory-Leibniz
    */
    private static double leibniz(int i) {
        return Math.pow(-1, i) * 4/(1 + 2*i);
    }
}
