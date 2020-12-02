import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 02/12/20
 */
public class TimeServer {
    public static final int MIN_SLEEP = 1000; // millisecondi minimi di attesa
    public static final int MAX_SLEEP = 3000; // millisecondi massimi di attesa
    public static final int PORT = 6789; // porta alla quale collegarsi

    public static void main(String[] args) {
        String dategroup;
        try {
            dategroup = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java TimeServer <dategrop>");
            return;
        }
        InetAddress address;
        try {
            address = InetAddress.getByName(dategroup);
        } catch (UnknownHostException e) {
            System.out.println("Usage: java TimeServer <dategrop>");
            return;
        }
        if (!address.isMulticastAddress()) {
            System.out.println("L'indirizzo " + address.getHostAddress() + " non è multicast.");
            return;
        }

        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        DatagramPacket sendPacket;
        String stringMessage;
        while (true) {
            byte[] message = (stringMessage = LocalDateTime.now().toString()).getBytes(StandardCharsets.UTF_8);

            sendPacket = new DatagramPacket(
                    message,
                    message.length,
                    address,
                    PORT
            );

            try {
                socket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Ho inviato " + stringMessage);

            try {
                Thread.sleep(getRandom(MIN_SLEEP, MAX_SLEEP));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Restituisce un numero intero pseudocasuale nell'intervallo [start, end]
     * @param start the least value returned
     * @param end the upper bound (inclusive)
     * @return pseudorandom integer number in range [start, end]
     */
    public static int getRandom(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

}
