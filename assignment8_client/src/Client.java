import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by alessiomatricardi on 25/11/20
 */
public class Client implements Runnable {
    private static final int SERVER_PORT = 2500; // porta del server
    private String msg;

    public Client(String message) {
        msg = message;
    }

    @Override
    public void run() {
        System.out.println("Voglio inviare questo messaggio: ");
        System.out.println(msg+"\n");
        System.out.println("Cerco di connettermi al server disponibile alla porta " + SERVER_PORT);

        SocketChannel socket = null;
        try {
            socket = SocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
            socket.connect(address); // bloccante

            byte[] byteMessage = msg.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(byteMessage);

            socket.write(buffer);
            buffer.clear();

            String readed;
            System.out.println("Risposta del server:\n");
            while(socket.read(buffer) > 0) {
                buffer.flip();

                readed = StandardCharsets.UTF_8.decode(buffer).toString();
                System.out.print(readed);

                buffer.clear();
            }

            System.out.println("\nChiudo connessione");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    e.printStackTrace();
                }
            }
        }
    }
}
