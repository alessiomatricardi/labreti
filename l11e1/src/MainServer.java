import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by alessiomatricardi on 10/12/20
 */
public class MainServer {
    public static final int SERVICE_PORT = 6789;
    public static final int REGISTRY_PORT = 7890;
    public static final String SERVICE_NAME = "RegistrazioneEvento";

    public static void main(String[] args) throws RemoteException {
        // creo oggetto
        MyServer server = new MyServer();
        // esporto stub
        Server stub = (Server) UnicastRemoteObject.exportObject(server, SERVICE_PORT);

        // creo registry
        Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        // registro servizio
        try {
            registry.bind(SERVICE_NAME, stub);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

}
