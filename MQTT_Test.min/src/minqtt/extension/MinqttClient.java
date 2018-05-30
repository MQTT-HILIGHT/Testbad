package minqtt.extension;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.SocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.ScheduledExecutorPingSender;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MinqttClient extends MqttClient implements IMqttClient {

	private String topic;

	public MinqttClient(String serverURI, String clientId) throws MqttException {
		super(serverURI, clientId);
		// TODO Auto-generated constructor stub
	} // ), DestinationProvider {
		// private static final String CLASS_NAME = MqttClient.class.getName();
		// private static final Logger log =
		// LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT,CLASS_NAME);

	public MinqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
		super(serverURI, clientId, persistence);
	}

	public void publish(String topic, MqttMessage message) throws MqttException, MqttPersistenceException {
		aClient.publish(topic, message, null, null);
	}
	
	public void setTopic(String topic) {
		this.topic=topic;
		
	}
	public String getTopic() {
		return this.topic;
	}
	
}
