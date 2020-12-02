import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 02/12/20
 */
public class TimeClient {
    public static final int PORT = 6789; // porta alla quale attendere
    public static final int BUFFER_LENGTH = 100; // lunghezza del buffer

    public static void main(String[] args) {
        String dategroup;
        try {
            dategroup = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java TimeClient <dategrop>");
            return;
        }
        InetAddress address;
        try {
            address = InetAddress.getByName(dategroup);
        } catch (UnknownHostException e) {
            System.out.println("Usage: java TimeClient <dategrop>");
            return;
        }
        if (!address.isMulticastAddress()) {
            System.out.println("L'indirizzo " + address.getHostAddress() + " non è multicast.");
            return;
        }

        MulticastSocket multicastSocket;
        try {
            multicastSocket = new MulticastSocket(PORT);
            multicastSocket.joinGroup(address);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] buffer = new byte[BUFFER_LENGTH];
        DatagramPacket receivePacket = new DatagramPacket(buffer, BUFFER_LENGTH);

        try {
            for (int i = 0; i < 10; i++) {
                multicastSocket.receive(receivePacket);

                String message = new String(
                        receivePacket.getData(),
                        receivePacket.getOffset(),
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8
                );

                System.out.format("%d - Ho ricevuto dal server: %s\n", i, message);
            }
            System.out.println("Termino");
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            multicastSocket.close();
        }
    }
}
