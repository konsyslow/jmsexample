/**
 * Created by admin on 12.05.2017.
 */
public class Main1 {
    public static void main(String[] args) {
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyMessageConsumer myMessageConsumer = new MyMessageConsumer();
        myMessageConsumer.receiveMessage();

    }
}
