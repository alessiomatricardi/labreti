import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 28/11/20
 */
public class PingClient {
    public static final int TIMEOUT = 2000; // 2 secondi
    public static final int TIMES = 10; // numero di volte che ripeto il ping

    public static void main(String[] args) {
        InetSocketAddress serverAddress;
        // check argomenti
        try {
            InetAddress address = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            serverAddress = new InetSocketAddress(address, port);
        } catch (ArrayIndexOutOfBoundsException e) { // mancano parametri
            System.out.println("Usage: java PingClient hostname port");
            return;
        } catch (UnknownHostException e) { // non riesco a risolvere l'hostname
            System.out.println("ERR -arg 1");
            return;
        } catch (NumberFormatException e) { // non riesco a parsare la porta
            System.out.println("ERR -arg 2");
            return;
        }

        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT); // receive bloccante per TIMEOUT ms
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        try {
            int receivedPackets = 0; // pacchetti di risposta ricevuti
            int maxRTT = -1; // max RTT
            int minRTT = 2001; // min RTT
            int sumRTT = 0; // somma degli RTT per tutti i pacchetti di risposta
            byte[] receiveBuffer = new byte[1];
            DatagramPacket receivePacket = new DatagramPacket(
                    receiveBuffer,
                    receiveBuffer.length
            );

            for (int i = 0; i < TIMES; i++) {
                long startTimestamp = System.currentTimeMillis(); // quando invio messaggio
                String message = "PING " + i + " " + startTimestamp;
                System.out.print(message + " RTT: ");

                byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(
                        byteMessage,
                        byteMessage.length,
                        serverAddress
                );

                socket.send(sendPacket);

                try {
                    socket.receive(receivePacket);

                    // ho ricevuto risposta
                    long endTimestamp = System.currentTimeMillis(); // quando ho ricevuto il messaggio
                    int rtt = (int) (endTimestamp - startTimestamp);
                    receivedPackets++;
                    sumRTT += rtt;
                    if (rtt > maxRTT) {
                        maxRTT = rtt;
                    }
                    if (rtt < minRTT) {
                        minRTT = rtt;
                    }
                    System.out.println(rtt + " ms");
                } catch (SocketTimeoutException e) {
                    System.out.println("*"); // non ho ricevuto risposta
                }
            }
            socket.close();

            // stampo statistiche
            System.out.println("---- PING Statistics ----");
            System.out.format("%d packets transmitted, %d packets received, %d%% packet loss\n",
                    TIMES, receivedPackets, (TIMES - receivedPackets) * 10);
            if (receivedPackets > 0)
                System.out.format("round-trip (ms) min/avg/max = %d/%.2f/%d\n",
                        minRTT, ((float) sumRTT) / receivedPackets, maxRTT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
