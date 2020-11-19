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
    private static final String CRLF = "\r\n";
    private static final String OK_200_HEADER = "HTTP/1.1 200 OK" + CRLF;
    private static final String NOT_FOUND_404_HEADER = "HTTP/1.1 404 Not Found" + CRLF;

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
            String requestLine = "";
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

            System.out.println(request);
            String filename = getRequestedFilename(requestLine);
            //System.out.println(filename);

            File file = new File(filename);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                byte[] byteFile = new byte[(int) file.length()];
                fileInputStream.read(byteFile);

                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
                outToClient.write(getResponseHeader(mimeType, file.length()));
                outToClient.write(byteFile);
                outToClient.flush();

                fileInputStream.close();

            }
            catch (FileNotFoundException e) {
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

    private byte[] getResponseHeader(String mimeType, long length) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

        String header = OK_200_HEADER
                        + "Server: localhost - Assignment 7" + CRLF
                        + "Date: " + formatter.format(new Date()) + CRLF
                        + "Content-Type: " + mimeType + "; charset=UTF-8" + CRLF
                        + "Connection: close" + CRLF
                        + "Content-Length: " + length
                        + CRLF + CRLF;
        return header.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] getNotFoundResponse() {
        return NOT_FOUND_404_HEADER.getBytes(StandardCharsets.UTF_8);
    }
}
