import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alessiomatricardi on 19/11/20
 */
public class Worker implements Runnable {
    private Socket connection;
    private static final String CRLF = "\r\n"; // codifica EOF
    private static final String OK_200_HEADER_LINE = "HTTP/1.1 200 OK" + CRLF;
    private static final String NOT_FOUND_404_HEADER_LINE = "HTTP/1.1 404 Not Found" + CRLF;

    public Worker(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            BufferedReader readFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());


            // ottieni richiesta
            StringBuilder request = new StringBuilder();
            String line;
            String requestLine = ""; // prima riga di una richiesta HTTP
            while ((line = readFromClient.readLine()) != null) {
                if (requestLine.isEmpty()) {
                    requestLine = line;
                }
                request.append(line);
                request.append('\n');
                if (line.isEmpty()) {
                    break;
                }
            }

            //System.out.println(request);
            String filename = getRequestedFilename(requestLine);
            //System.out.println(filename);

            File file = new File(filename);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                // scrivo il file come sequenza di byte su un array
                byte[] byteFile = new byte[(int) file.length()];
                fileInputStream.read(byteFile);

                // from https://www.baeldung.com/java-file-mime-type
                String mimeType = URLConnection.guessContentTypeFromName(file.getName());

                // scrivo response header e data su outputstream del socket
                outToClient.write(getResponseHeader(mimeType, file.length()));
                outToClient.write(byteFile);
                outToClient.flush();

                fileInputStream.close();

            }
            catch (FileNotFoundException e) {
                // se il file non è stato trovato, il response header sarà 404 Not Found
                outToClient.write(getNotFoundResponse());
                outToClient.flush();
            }

            readFromClient.close();
            outToClient.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * restituisce il path del file richiesto dal client
     * se il path non fa riferimento a nessun file specifico,
     * restituisce il file index.html appartenente a quel path.
     *
     * @param requestLine request line dell'header HTTP
     * @return path del file
     *
     * */
    private String getRequestedFilename(String requestLine) {
        /*
         * Secondo RFC 7230
         * https://tools.ietf.org/html/rfc7230#section-3.1.1
         * request-line = method SP request-target SP HTTP-version CRLF
         */
        String[] tokens = requestLine.split(" ");
        if (tokens[1].endsWith("/")) {
            return tokens[1]+"index.html";
        }
        return tokens[1];
    }

    /**
     * restituisce un header di risposta HTTP con status code 200
     * completo dei campi
     * - Server
     * - Data
     * - Content-Type
     * - Connection
     * - Content-Length
     * in formato array di byte
     *
     *
     * @param mimeType MIME type (Content-Type)
     * @param length lunghezza del file in byte
     * @return header di risposta HTTP in formato array di byte
     *
     * */
    private byte[] getResponseHeader(String mimeType, long length) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

        String header = OK_200_HEADER_LINE
                        + "Server: localhost - Assignment 7" + CRLF
                        + "Date: " + formatter.format(new Date()) + CRLF
                        + "Content-Type: " + mimeType + "; charset=UTF-8" + CRLF
                        + "Connection: close" + CRLF
                        + "Content-Length: " + length
                        + CRLF + CRLF;
        return header.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Restituisce header di risposta HTTP 404 Not Found
     * */
    private byte[] getNotFoundResponse() {
        return NOT_FOUND_404_HEADER_LINE.getBytes(StandardCharsets.UTF_8);
    }
}
