import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alessiomatricardi on 10/12/20
 */
public class MyServer extends RemoteServer implements Server {
    List<String> registeredUsers;

    public MyServer() throws RemoteException {
        registeredUsers = new ArrayList<String>();
    }

    @Override
    public synchronized void register(String username) throws RemoteException {
        registeredUsers.add(username);
    }

    @Override
    public synchronized List<String> getRegisteredUsers() throws RemoteException {
        return registeredUsers;
    }
}
