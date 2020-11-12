import java.util.List;
import java.util.Map;

/**
 * Created by alessiomatricardi on 12/11/2020
 */
public class Analizzatore implements Runnable {
    Map<String,SafeCounter> counters; // contatori
    ContoCorrente contoCorrente; // conto corrente da analizzare

    public Analizzatore(Map<String,SafeCounter> counters, ContoCorrente contoCorrente) {
        this.counters = counters;
        this.contoCorrente = contoCorrente;
    }

    @Override
    public void run() {
        List<ContoCorrente.Movimento> movimenti = contoCorrente.getMovimenti();

        for (ContoCorrente.Movimento mov : movimenti) {
            SafeCounter c = counters.get(mov.getCausale());
            if (c != null) {
                c.increment();
            }
        }
    }
}
