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
        if (messageToSend.equals("")) {
            System.out.println("Il messaggio non può essere vuoto");
            return;
        }

        new Thread(new Client(messageToSend)).start();
    }
}
