package com.modestasc.bambuflow_backend.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BambuMqttClient {
    private final MqttClientFactory factory;
    private MqttClient client;

    // Default constructor for real use
    public BambuMqttClient() {
        this((uri, clientId) -> new MqttClient(uri, clientId));
    }

    // Constructor for tests
    public BambuMqttClient(MqttClientFactory factory) {
        this.factory = factory;
    }

    public void connect(String hostIp, String serial, String accessCode) throws Exception {
        String uri = "ssl://" + hostIp + ":8883";
        client = factory.create(uri, MqttClient.generateClientId());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("bblp");
        options.setPassword(accessCode.toCharArray());

        options.setSSLHostnameVerifier((h, s) -> true);
        options.setHttpsHostnameVerificationEnabled(false);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{ new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] c, String a) {}
            public void checkServerTrusted(X509Certificate[] c, String a) {}
        }}, new SecureRandom());
        options.setSocketFactory(sslContext.getSocketFactory());

        client.connect(options);

        String topic = "device/" + serial + "/report";
        client.subscribe(topic, (t, msg) -> {
            String json = new String(msg.getPayload(), UTF_8);
            System.out.println(json);
        });
    }

    public void close() throws Exception {
        if (client != null) {
            if (client.isConnected()) client.disconnect();
            client.close();
        }
    }
}
