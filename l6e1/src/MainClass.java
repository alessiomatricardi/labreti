import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by alessiomatricardi on 11/11/2020
 */
public class MainClass {
    public static final int BUFFER_SIZE = 4096;
    public static final String fileOut = "frequency";
    public static final int EOS = -1;
    public static final int A_POS = 65;
    public static final int Z_POS = 90;
    public static final int FREQUENCE_SIZE = Z_POS - A_POS + 1;

    public static void main (String[] args) throws IOException {
        // check numero argomenti
        if (args.length != 1) {
            System.out.println("Usage: java MainClass <text file>");
            return;
        }
        String fileIn = args[0];

        FileChannel inChannel = FileChannel.open(Paths.get(fileIn), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int[] frequency = new int[FREQUENCE_SIZE];

        int bytesRead = 0;
        do {
            bytesRead = inChannel.read(buffer);
            buffer.flip();

            while(buffer.hasRemaining()) {
                byte c = buffer.get();
                System.out.format("Read character '%c'\n", (char)c);
                int uc = Character.toUpperCase(c);
                if (uc <= Z_POS && uc >= A_POS) {
                    frequency[uc - A_POS] += 1;
                }
            }

            buffer.clear();
        } while (bytesRead != EOS);
        inChannel.close();

        FileWriter fileWriter = new FileWriter(new File(fileOut));
        for (int i = 0; i < frequency.length; i++) {
            System.out.format("frequence of %c is %d\n", 'A' + i, frequency[i]);
            String s = String.format("%c = %d\n", 'A' + i, frequency[i]);
            fileWriter.write(s);
        }
        fileWriter.close();

    }
}
