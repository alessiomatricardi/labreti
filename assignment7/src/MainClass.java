import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alessiomatricardi on 19/11/20
 */
public class MainClass {
    public static int port = 6789;

    public static void main(String[] args) {
        ServerSocket server;

        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server non disponibile sulla porta " + port);
            return;
        }

        Socket connectionSocket;
        ExecutorService threadPool = Executors.newCachedThreadPool();
        while (true) {
            try {
                connectionSocket = server.accept();

                threadPool.submit(new Worker(connectionSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}