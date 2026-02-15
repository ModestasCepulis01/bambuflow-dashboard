package com.modestasc.bambuflow_backend.mqtt.UT;

import com.modestasc.bambuflow_backend.mqtt.BambuMqttClient;
import com.modestasc.bambuflow_backend.mqtt.interfaces.MqttClientFactory;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BambuMqttClientTest {
    @Test
    void connect_callsConnectAndSubscribesToReportTopic() throws Exception{
        //Arrange
        MqttClient mockClient = mock(MqttClient.class);

        MqttClientFactory mockFactory = (uri, clientId) -> {
            if (!uri.equals("ssl://192.168.1.1:8883")) {
                throw new AssertionError("Wrong URI: " + uri);
            }
            return mockClient;
        };

        BambuMqttClient bambu = new BambuMqttClient(mockFactory);

        //Act
        bambu.connect("192.168.1.1", "SERIAL123", "CODE1234", json -> {});

        //Assert
        verify(mockClient).connect(any(MqttConnectOptions.class));
        verify(mockClient).subscribe(eq("device/SERIAL123/report"), any(IMqttMessageListener.class));
    }

    @Test
    void close_disconnectsIfConnected_andClosesClient() throws Exception {
        MqttClient mockClient = mock(MqttClient.class);
        when(mockClient.isConnected()).thenReturn(true);

        BambuMqttClient bambu = new BambuMqttClient((u, id) -> mockClient);

        doNothing().when(mockClient).connect(any());
        doNothing().when(mockClient).subscribe(anyString(), any(IMqttMessageListener.class));

        bambu.connect("192.168.1.1", "SERIAL123", "CODE1234", json -> {});
        bambu.close();

        verify(mockClient).disconnect();
        verify(mockClient).close();
    }
}
