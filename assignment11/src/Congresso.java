import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.*;

/**
 * Created by alessiomatricardi on 15/12/20
 */
public class Congresso extends RemoteServer implements CongressoInt {
    public static final String SERVICE_NAME = "congresso";
    public static final int NUM_OF_DAYS = 3;
    public static final int NUM_OF_SESSIONS = 12;
    public static final int MAX_SPEAKERS = 5;
    private List<List<String>>[] giornate;

    public Congresso() throws RemoteException {
        giornate = new List[NUM_OF_DAYS];
        for (int i = 0; i < NUM_OF_DAYS; i++) {
            giornate[i] = new ArrayList<>();
            for (int j = 0; j < NUM_OF_SESSIONS; j++) {
                giornate[i].add(new ArrayList<>());
            }
        }
    }

    public synchronized void registerToSession(String speaker, int day, int session)
        throws RemoteException, InvalidDayException, InvalidSessionException, FullSessionException, SpeakerAlreadyPresentException {
        if (day < 1 || day > NUM_OF_DAYS) throw new InvalidDayException();
        if (session < 1 || session > NUM_OF_SESSIONS) throw new InvalidSessionException();
        if (giornate[day-1].get(session-1).size() == MAX_SPEAKERS) throw new FullSessionException();
        if (giornate[day-1].get(session-1).contains(speaker)) throw new SpeakerAlreadyPresentException();
        giornate[day-1].get(session-1).add(speaker);
    }

    public synchronized List<List<String>>[] getSchedule() throws RemoteException {
        return giornate;
    }

}