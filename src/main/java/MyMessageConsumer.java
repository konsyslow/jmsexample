import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by admin on 12.05.2017.
 */
public class MyMessageConsumer {

    public Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("vm://localhost");
        return activeMQConnectionFactory.createConnection();
    }

    public void receiveMessage(){
        Session session = null;
        try {
            Connection connection = createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("MyQueue");
            MessageConsumer messageConsumer = session.createConsumer(destination);
            Message message = messageConsumer.receive(10000);
            System.out.println(((TextMessage)message).getText());
            messageConsumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
