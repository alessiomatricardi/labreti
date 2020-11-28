import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by alessiomatricardi on 28/11/20
 */
public class PingServer {
    public static final int MAX_DELAY = 300; // ritardo di massimo 300 millisecondi

    public static void main(String[] args) {
        int port;
        // check argomento
        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java PingServer port");
            return;
        } catch (NumberFormatException e) {
            System.out.println("ERR -arg 1");
            return;
        }

        DatagramSocket socket;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("ERR -arg 1");
            return;
        }

        byte[] buffer = new byte[150];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            while (true) {
                socket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress(); // ip mittente
                int clientPort = receivePacket.getPort(); // porta mittente
                System.out.format("%s:%d> ", clientAddress.getHostAddress(), clientPort);
                System.out.format("%s ACTION: ", new String(
                        receivePacket.getData(),
                        receivePacket.getOffset(),
                        receivePacket.getLength(),
                        StandardCharsets.UTF_8)
                );
                if (getRandomNumber(1,4) == 1) { // Prob (numero = 1) = 0.25
                    System.out.println("not sent");
                } else { // Prob (numero > 1) = 0.75
                    int delay = getRandomNumber(1, MAX_DELAY); // randomizzo delay
                    System.out.format("delayed %d ms\n", delay);

                    Thread.sleep(delay);

                    DatagramPacket sendPacket = new DatagramPacket(
                            receivePacket.getData(),
                            receivePacket.getOffset(),
                            receivePacket.getLength(),
                            clientAddress,
                            clientPort
                    );
                    socket.send(sendPacket);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            socket.close();
        }
    }

    /**
     * Restituisce un numero intero pseudocasuale nell'intervallo [start, end]
     * @param start the least value returned
     * @param end the upper bound (inclusive)
     * @return pseudorandom integer number in range [start, end]
     * */
    public static int getRandomNumber(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

}
