package com.modestasc.bambuflow_backend.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

@FunctionalInterface
public interface MqttClientFactory {
    MqttClient create(String uri, String clientId) throws MqttException;
}
