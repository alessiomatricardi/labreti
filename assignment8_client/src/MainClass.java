/**
 * Created by alessiomatricardi on 25/11/20
 */
public class MainClass {

    public static void main (String[] args) {
        String messageToSend = null;
        try {
            messageToSend = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: Mainclass <message to send>");
            return;
        }

        assert messageToSend != null;

        new Thread(new Client(messageToSend)).start();
    }
}
