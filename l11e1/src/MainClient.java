import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Created by alessiomatricardi on 10/12/20
 */
public class MainClient {
    public static final String SERVICE_NAME = "RegistrazioneEvento";
    public static final int REGISTRY_PORT = 7890;

    public static void main(String[] args) {
        String username;

        try {
            username = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java MainClient <username>");
            return;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);
            Server server = (Server) registry.lookup(SERVICE_NAME);
            System.out.println(username + ": mi aggiungo alla lista");
            server.register(username);
            List<String> lista = server.getRegisteredUsers();
            System.out.println("Ci sono " + lista.size() + " utenti registrati.\nLa lista degli utenti è:");
            for(String user : lista) {
                System.out.println(user);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
