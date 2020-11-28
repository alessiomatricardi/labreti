import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 27/11/20
 */
public class Server implements Runnable {
    public static final int PORT = 2500;
    public static final int TIMEOUT = 20000;

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            socket.setSoTimeout(TIMEOUT);

            byte[] buffer = new byte[5];

            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            byte[] bytePong = "Pong".getBytes(StandardCharsets.UTF_8);

            while (true) {
                try {
                    socket.receive(receivePacket);
                    System.out.println("Client: " + new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8));
                } catch (SocketTimeoutException e) {
                    System.out.println("Il client ha smesso di inviare...");
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DatagramPacket sendPacket = new DatagramPacket(bytePong, bytePong.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
