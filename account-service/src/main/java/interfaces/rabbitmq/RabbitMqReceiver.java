package interfaces.rabbitmq;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import messaging.Event;
import messaging.EventReceiver;

import java.util.logging.Logger;
import java.util.logging.Level;

public class RabbitMqReceiver {
    private final static Logger LOGGER = Logger.getLogger(RabbitMqReceiver.class.getName());
	private static final String EXCHANGE_NAME = "message-hub";
	private static final String QUEUE_TYPE = "topic";
	private static final String TOPIC = "account.*";

	EventReceiver service;

	public RabbitMqReceiver(EventReceiver service) {
		this.service = service;
	}

	public void listen() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbitMq");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, TOPIC);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
            LOGGER.log(Level.INFO, "RABBITMQ: Received raw message: " + message);

            Event event;

			try {
                event = new Gson().fromJson(message, Event.class);
			} catch (Exception e) {
                LOGGER.log(Level.WARNING, "RABBITMQ: Failed to parse message to JSON: " + message);
                return;
			}

            try {
                service.receiveEvent(event);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "RABBITMQ: Receive event error: " + e.getMessage());
            }
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

}