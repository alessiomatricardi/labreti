import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 15/12/20
 */
public class MainClient {
    public static final int REG_PORT = 6789;
    public static final String[] nomi = {"Alessio", "Francesco", "Stefano", "Giovanni", "Giulia",
            "Samuele", "Davide", "Francesca", "Benedetta", "Roberto", "Marco", "Mario", "Bruno", "Giuseppe"};

    public static String getRandomName() {
        int pos = ThreadLocalRandom.current().nextInt(0, nomi.length);
        return nomi[pos];
    }

    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry(REG_PORT);
            CongressoInt congresso = (CongressoInt) reg.lookup(Congresso.SERVICE_NAME);

            for (int day = 1; day <= Congresso.NUM_OF_DAYS; day++) {
                for (int session = 1; session <= Congresso.NUM_OF_SESSIONS; session++) {
                    int count = 0;
                    while (count < Congresso.MAX_SPEAKERS) {
                        String person = getRandomName();
                        try {
                            congresso.registerToSession(person, day, session);
                            count++;
                        } catch (InvalidSessionException e) {
                            System.out.format("Session %d is not valid\n", session);
                            break;
                        } catch (FullSessionException e) {
                            System.out.format("Session %d of day %d is full\n", session, day);
                            break;
                        } catch (InvalidDayException e) {
                            System.out.format("Day %d is not valid\n", day);
                            break;
                        } catch (SpeakerAlreadyPresentException e) {
                            System.out.format("%s is already present in session %d of day %d\n", person, session, day);
                        }
                    }
                }
            }

            // stampo la lista degli speakers
            List<List<String>>[] programma = congresso.getSchedule();
            for (int i = 0; i < programma.length; i++) {
                List<List<String>> day = programma[i];
                for (int j = 0; j < day.size(); j++) {
                    List<String> session = day.get(j);
                    System.out.format("Day %d - session %d:\n", i+1, j+1);
                    for (String speaker : session) {
                        System.out.println(speaker);
                    }
                }
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
