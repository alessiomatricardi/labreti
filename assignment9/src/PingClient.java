import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 28/11/20
 */
public class PingClient {
    public static final int TIMEOUT = 2000; // 2 secondi
    public static final int TIMES = 10;

    public static void main(String[] args) {
        InetSocketAddress serverAddress;
        // check argomenti
        try {
            InetAddress address = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);
            serverAddress = new InetSocketAddress(address, port);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java PingClient hostname port");
            return;
        } catch (UnknownHostException e) {
            System.out.println("ERR -arg 1");
            return;
        } catch (NumberFormatException e) {
            System.out.println("ERR -arg 2");
            return;
        }

        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        try {
            int receivedPackets = 0;
            int maxRTT = -1;
            int minRTT = 2001;
            int sumRTT = 0;
            byte[] receiveBuffer = new byte[1];
            DatagramPacket receivePacket = new DatagramPacket(
                    receiveBuffer,
                    receiveBuffer.length
            );

            for (int i = 0; i < TIMES; i++) {
                long startTimestamp = System.currentTimeMillis();
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

                    long endTimestamp = System.currentTimeMillis();
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
                    System.out.println("*");
                }
            }
            socket.close();

            System.out.println("---- PING Statistics ----");
            System.out.format("%d packets transmitted, %d packets received, %d%% packet loss\n",
                    TIMES, receivedPackets, (TIMES-receivedPackets)*10);
            if (receivedPackets > 0)
                System.out.format("round-trip (ms) min/avg/max = %d/%.2f/%d\n",
                    minRTT, ((float)sumRTT)/receivedPackets, maxRTT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
