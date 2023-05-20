package com.smartoffice.mqtt.BackUp;

import org.eclipse.paho.client.mqttv3.*;

public class LightPublisher {

    private static MqttClient pClient;
    private static String topicLight = "floor/light/ID";
    private static String topicWindow = "floor/window/ID";

    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientID = "Publisher";
        String lightStatus = getLightStatus();
        String windowLocation = getWindowLocation();

        try {
            pClient = new MqttClient(broker, clientID);
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(180);

            // Connect
            System.out.println("Connecting to broker: " + broker);
            pClient.connect(connOpts);
            System.out.println("Connected...");

            // Send the messages
            publishMessage(topicLight, String.valueOf(lightStatus), 1, false);
            publishMessage(topicWindow, String.valueOf(windowLocation), 1, false);

            Thread.sleep(500); // Wait for 0.5 second

            // Disconnect the connection
            pClient.disconnect();
            System.out.println("Disconnected...");
            pClient.close();
            System.exit(0);

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void publishMessage(String topic, String payload, int qos, boolean retained) {
        System.out.println("Publishing message: " + payload + " on topic: " + topic);

        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            message.setRetained(retained);
            pClient.publish(topic, message);
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            System.out.println("Reason: " + e.getReasonCode());
            e.printStackTrace();
        }

        System.out.println("Message published...");
    }

    private static String getLightStatus() {
        return Math.random() < 0.5 ? "ON" : "OFF";
    }

    private static String getWindowLocation() {
        return Math.random() < 0.5 ? "OPEN" : "CLOSE";
    }
}
