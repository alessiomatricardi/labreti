import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by alessiomatricardi on 15/12/20
 */
public interface CongressoInt extends Remote {

    /**
     * @param speaker nome dello speaker
     * @param day giorno dell'evento
     * @param session sessione della giornata
     *
     * @throws RemoteException se ci sono errori legati alla rete
     * @throws InvalidSessionException se session non è nel range [1, 12]
     * @throws InvalidDayException se day non è nel range [1, 3]
     * @throws FullSessionException se la sessione di quella giornata è già completa
     * @throws SpeakerAlreadyPresentException se lo speaker è già registrato alla sessione
     *
     */
    void registerToSession (String speaker, int day, int session)
            throws RemoteException, InvalidSessionException,
            FullSessionException, InvalidDayException, SpeakerAlreadyPresentException;
    /**
     *
     * @throws RemoteException se ci sono errori legati alla rete
     *
     */
    List<List<String>>[] getSchedule() throws RemoteException;

}
