import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 27/11/20
 */
public class Client implements Runnable {
    public static final int PORT = 2500;
    public static final int TIMEOUT = 20000;

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);

            byte[] buffer = new byte[5];

            byte[] bytePing = "Ping".getBytes(StandardCharsets.UTF_8);

            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(bytePing, bytePing.length, address, PORT);

            for(int i = 0; i < 10; i++) { // condizione per farli terminare
                socket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(receivePacket);
                    System.out.println("Server: " + new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8));
                } catch (SocketTimeoutException e) {
                    System.out.println("Il server ha smesso di inviare...");
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
