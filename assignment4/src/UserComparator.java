import java.util.Comparator;

/**
 * Created by alessiomatricardi on 12/10/2020
 */
public class UserComparator implements Comparator<Utente> {

    @Override
    public int compare(Utente utente1, Utente utente2) {
        if ((utente1 instanceof Professore && utente2 instanceof Professore)
            || (utente1 instanceof Tesista && utente2 instanceof Tesista)
            || (utente1 instanceof Studente && utente2 instanceof Studente)) {

            return utente1.getUltimaPrenotazione().compareTo(utente2.getUltimaPrenotazione());
        }
        if (utente1 instanceof Professore) return -1;
        if (utente2 instanceof Professore) return 1;
        if (utente1 instanceof Tesista) return -1;
        return 1;
    }
}
