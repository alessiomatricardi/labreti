/**
 * Created by alessiomatricardi on 27/11/20
 */
public class MainClassServer {
    public static void main(String[] args) {

        new Thread(new Server()).start();

    }
}
