import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by alessiomatricardi on 10/12/20
 */
public interface Server extends Remote {

    void register(String username) throws RemoteException;

    List<String> getRegisteredUsers() throws RemoteException;
}
