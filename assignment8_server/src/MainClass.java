/**
 * Created by alessiomatricardi on 26/11/20
 */
public class MainClass {

    public static void main (String[] args) {

        new Thread(new Server()).start();
    }
}
