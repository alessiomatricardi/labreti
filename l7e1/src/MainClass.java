import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alessiomatricardi on 18/11/20
 */
public class MainClass {
    public static int port = 7000;
    public static final String filename = "file.txt";

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server in ascolto sulla porta " + port);
            } catch (IOException e) {
                System.out.println("Porta " + port++ + " non disponibile");
            }
        }

        Socket connSocket;
        try {
            connSocket = serverSocket.accept();
            System.out.println("Client " + connSocket.getInetAddress() + " connesso!");
            DataOutputStream outputStream = new DataOutputStream(connSocket.getOutputStream());

            File file = new File(filename);
            FileInputStream inputStream = new FileInputStream(file);

            byte[] byteFile = new byte[(int) file.length()];

            if (inputStream.read(byteFile) != -1) {
                outputStream.write(byteFile);
                outputStream.flush();
            }

            connSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
