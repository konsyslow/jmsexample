/**
 * Created by admin on 12.05.2017.
 */
public class Main2 {
    public static void main(String[] args) {
        MyMessageProducer myMessageProducer = new MyMessageProducer();
        myMessageProducer.sendMessage("asdsfdfadf");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
