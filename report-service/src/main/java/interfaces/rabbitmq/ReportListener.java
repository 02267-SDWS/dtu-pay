/**
 * @primary-author Tobias (s173899)
 * @co-author Emil (s174265)
 *
 * RabbitMQ listener based on code given by Hubert
 */

package interfaces.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import infrastructure.ConfigService;
import infrastructure.IConfigService;
import messaging.Event;
import messaging.EventReceiver;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportListener {

	private final static Logger LOGGER = Logger.getLogger(ReportListener.class.getName());

	private static final String EXCHANGE_NAME = "message-hub";
	private static final String QUEUE_TYPE = "topic";
	private static final String TOPIC = "dtupay.*";

	EventReceiver service;

	public ReportListener(EventReceiver service) {
		this.service = service;
	}

	public void listen() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		IConfigService config = new ConfigService();
		String rabbitMQhost = config.getProp("rabbitmq.host");
        LOGGER.info("CONNECTING TO RABBITMQ HOST: " + rabbitMQhost);
        factory.setHost(rabbitMQhost);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, TOPIC);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
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


	/*
	private static final String EXCHANGE_NAME = "message-hub";
	private static final String QUEUE_TYPE = "topic";

	EventReceiver service;

	public RabbitMQReceiver(EventReceiver service) {
		this.service = service;
	}

	public void initConnection(String topic) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		IConfigService config = new ConfigService();
		factory.setHost(config.getProp("rabbitmq.host"));
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, topic);

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			System.out.println("[x] receiving "+message);

			Event event = new Gson().fromJson(message, Event.class);
			try {
				service.receiveEvent(event);
			} catch (Exception e) {
				throw new Error(e);
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}

	 */
}
