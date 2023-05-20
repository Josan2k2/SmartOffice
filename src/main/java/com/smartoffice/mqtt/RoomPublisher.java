package com.smartoffice.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class RoomPublisher {

    private static MqttClient pClient;

    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientID = "Publisher";
        double temperature = getRandomTemperature();
        double humidity = getRandomHumidity();

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
            publishMessage("floor/room/temperature", String.valueOf(temperature), 1, false);
            publishMessage("floor/room/humidity", String.valueOf(humidity), 2, false);

            Thread.sleep(1000); // Wait for 1 second

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

    private static double getRandomTemperature() {
        // Generate random temperature
        // Replace with your own implementation
        return Math.random() * 30 + 15; // Example: random temperature between 15 and 45 degrees Celsius
    }

    private static double getRandomHumidity() {
        // Generate random humidity
        // Replace with your own implementation
        return Math.random() * 60 + 40; // Example: random humidity between 40% and 100%
    }
}
