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
 * Created by alessiomatricardi on 25/11/20
 */
public class MainClass {
    public static final int PORT = 2500;

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(PORT);
            serverSocket.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        assert selector != null;

        while (true) {
            try {
                selector.select(); // ritorna subito
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator <SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove(); // rimuovo da chiavi ready

                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    try {
                        SocketChannel client = server.accept(); // sicuramente non si bloccherà

                        client.configureBlocking(false);
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
                        ByteBuffer buffer = ByteBuffer.allocate(100); // alloco 100 byte
                        key2.attach(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            server.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    if (buffer != null) {
                        buffer.put("Hello World!\n".getBytes(StandardCharsets.UTF_8));

                        buffer.flip();
                        try {
                            while(buffer.hasRemaining()) {
                                client.write(buffer);
                            }
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                client.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
