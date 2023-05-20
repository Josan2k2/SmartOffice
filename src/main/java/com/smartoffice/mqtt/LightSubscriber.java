package com.smartoffice.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class LightSubscriber {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientID = "Subscriber";

        try {
            MqttClient sClient = new MqttClient(broker, clientID);
            MqttConnectOptions connOptions = new MqttConnectOptions();

            connOptions.setCleanSession(true);

            sClient.setCallback(new lSubscriber());

            System.out.println("Connecting to broker: " + broker);
            sClient.connect(connOptions);
            System.out.println("Connected...");

            sClient.subscribe("floor/light/ID");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

class lSubscriber implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection is lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message received from topic: " + topic);
        System.out.println("Payload: " + new String(message.getPayload()));
        System.out.println("QoS: " + message.getQos());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery is complete: " + token.isComplete());
    }
}
