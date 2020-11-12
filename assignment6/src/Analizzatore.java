import java.util.List;
import java.util.Map;

/**
 * Created by alessiomatricardi on 12/11/2020
 */
public class Analizzatore implements Runnable {
    Map<String,SafeCounter> counters;
    ContoCorrente contoCorrente;

    public Analizzatore(Map<String,SafeCounter> counters, ContoCorrente contoCorrente) {
        this.counters = counters;
        this.contoCorrente = contoCorrente;
    }

    @Override
    public void run() {
        String name = contoCorrente.getNome();
        String surname = contoCorrente.getCognome();
        List<ContoCorrente.Movimento> movimenti = contoCorrente.getMovimenti();

        // System.out.format("Conto corrente di %s %s\n", name, surname);

        for (ContoCorrente.Movimento mov : movimenti) {
            SafeCounter c = counters.get(mov.getCausale());
            if (c != null) {
                c.increment();
            }
        }
    }
}
