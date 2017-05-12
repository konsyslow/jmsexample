package chat;

import javax.jms.*;
import javax.naming.*;
import java.io.*;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by admin on 12.05.2017.
 */
public class Chat implements javax.jms.MessageListener {
    private TopicSession pubSession;
    private TopicSession subSession;
    private TopicPublisher publisher;
    private TopicConnection connection;
    private String username;

    public Chat(String topicName, String username, String password)
            throws Exception {
        Properties env = new Properties();

        InitialContext jndi = new InitialContext(env);

        TopicConnectionFactory conFactory =
                (TopicConnectionFactory) jndi.lookup("TopicConnectionFactory");

        TopicConnection connection =
                conFactory.createTopicConnection(username, password);

        TopicSession pubSession =
                connection.createTopicSession(false,
                        Session.AUTO_ACKNOWLEDGE);
        TopicSession subSession =
                connection.createTopicSession(false,
                        Session.AUTO_ACKNOWLEDGE);

        Topic chatTopic = (Topic) jndi.lookup(topicName);

        TopicPublisher publisher =
                pubSession.createPublisher(chatTopic);
        TopicSubscriber subscriber =
                subSession.createSubscriber(chatTopic);

        subscriber.setMessageListener(this);

        set(connection, pubSession, subSession, publisher, username);

        connection.start();

    }

    public void set(TopicConnection con, TopicSession pubSess,
                    TopicSession subSess, TopicPublisher pub,
                    String username) {
        this.connection = con;
        this.pubSession = pubSess;
        this.subSession = subSess;
        this.publisher = pub;
        this.username = username;
    }

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    protected void writeMessage(String text) throws JMSException {
        TextMessage message = pubSession.createTextMessage();
        message.setText(username + " : " + text);
        publisher.publish(message);
    }

    public void close() throws JMSException {
        connection.close();
    }

    public static void main(String[] args) {
        try {
            if (args.length != 3)
                System.out.println("Topic or username missing");

            // args[0]=topicName; args[1]=username; args[2]=password
            Chat chat = new Chat(args[0], args[1], args[2]);
            BufferedReader commandLine = new
                    java.io.BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String s = commandLine.readLine();
                if (s.equalsIgnoreCase("exit")) {
                    chat.close();
                    System.exit(0);
                } else
                    chat.writeMessage(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
