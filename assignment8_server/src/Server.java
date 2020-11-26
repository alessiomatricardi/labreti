import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by alessiomatricardi on 26/11/20
 */
public class Server implements Runnable {
    private static final int PORT = 2500; // porta del server
    private static final int ALLOCATION_SIZE = 512*512; // size (in byte) per allocazione di un ByteBuffer

    @Override
    public void run() {
        ServerSocketChannel serverChannel;
        Selector selector = null;

        try {
            serverChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(PORT);
            ServerSocket server = serverChannel.socket();
            server.bind(address);
            serverChannel.configureBlocking(false); // server socket non bloccante
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // registro server per accettare connessioni

            while (true) {
                selector.select();

                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) { // server pronto ad accettare connessione
                        ServerSocketChannel server1 = (ServerSocketChannel) key.channel();
                        try {
                            SocketChannel client = server1.accept(); // non si bloccherà

                            client.configureBlocking(false); // client channel non bloccante

                            // preparo per lettura da client
                            SelectionKey key2 = client.register(selector, SelectionKey.OP_READ);
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                server1.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            return;
                        }
                    } else if (key.isReadable()) { // client ha scritto su channel, sono pronto a leggerlo
                        SocketChannel client = (SocketChannel) key.channel();

                        ByteBuffer buffer = ByteBuffer.allocate(ALLOCATION_SIZE);

                        // read message from channel
                        StringBuilder message = new StringBuilder("");

                        while (client.read(buffer) > 0) {
                            buffer.flip();

                            message.append(StandardCharsets.UTF_8.decode(buffer));

                            buffer.clear();
                        }

                        // preparo messaggio da inviare e lo salvo sul buffer
                        message.append("\nechoed by server\n");
                        byte[] byteMessage = message.toString().getBytes(StandardCharsets.UTF_8);
                        buffer = ByteBuffer.wrap(byteMessage);

                        // preparo per scrittura su client
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE, buffer);
                    } else if (key.isWritable()) { // client aspetta scrittura su channel
                        SocketChannel client = (SocketChannel) key.channel();

                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        if (buffer == null) {
                            System.out.println("buffer is null");
                            client.close();
                            continue;
                        }

                        try {
                            client.write(buffer);
                        }
                        catch (IOException e) {
                            client.close();
                        }
                        buffer.clear();

                        // dopo aver scritto sul channel, chiudo la connessione con il client
                        client.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
