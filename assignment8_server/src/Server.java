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
    private static int PORT = 2500;

    @Override
    public void run() {
        ServerSocketChannel serverChannel;
        Selector selector = null;

        try {
            serverChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(PORT);
            ServerSocket server = serverChannel.socket();
            server.bind(address);
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();

                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        ServerSocketChannel server1 = (ServerSocketChannel) key.channel();
                        try {
                            SocketChannel client = server1.accept(); // sicuramente non si bloccherà

                            client.configureBlocking(false);

                            // preparo per lettura da client
                            SelectionKey key2 = client.register(selector, SelectionKey.OP_READ, new Message());
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                server1.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            return;
                        }
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();

                        Message message = (Message) key.attachment();

                        if (message == null) {
                            System.out.println("message is null");
                            client.close();
                            continue;
                        }

                        try {
                            message.readMessageFromChannel(client);
                        }
                        catch (IOException e) {
                            client.close();
                        }

                        // preparo per scrittura su client
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE, message);
                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();

                        Message message = (Message) key.attachment();

                        if (message == null) {
                            System.out.println("message is null");
                            client.close();
                            continue;
                        }

                        try {
                            message.writeMessageToChannel(client);
                        }
                        catch (IOException e) {
                            client.close();
                        }

                        client.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Message {
    private static int ALLOCATION_SIZE = 1024*1024;

    private StringBuilder message;

    public Message() {
        message = new StringBuilder("");
    }

    public void readMessageFromChannel (SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(ALLOCATION_SIZE);

        while (channel.read(buffer) > 0) {
            buffer.flip();

            message.append(StandardCharsets.UTF_8.decode(buffer));

            buffer.clear();
        }
    }

    public void writeMessageToChannel (SocketChannel channel) throws IOException {
        message.append("\nechoed by server\n");
        byte[] byteMessage = message.toString().getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.wrap(byteMessage);

        channel.write(buffer);

        buffer.clear();
    }
}
