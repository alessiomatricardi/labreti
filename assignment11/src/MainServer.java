import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by alessiomatricardi on 15/12/20
 */
public class MainServer {
    public static final int REG_PORT = 6789;
    public static final int SERVICE_PORT = 8901;

    public static void main(String[] args) throws RemoteException {
        CongressoInt s = new Congresso();

        CongressoInt stub = (CongressoInt) UnicastRemoteObject.exportObject(s, SERVICE_PORT);

        Registry reg = LocateRegistry.createRegistry(REG_PORT);

        try {
            reg.bind(Congresso.SERVICE_NAME, stub);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

}
