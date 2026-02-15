package com.modestasc.bambuflow_backend.mqtt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PrinterStateService {

    private final AtomicReference<JsonNode> latest = new AtomicReference<>();

    private final ObjectMapper mapper;

    private final BambuMqttClient mqttClient ;

    public PrinterStateService(BambuMqttClient mqttClient, ObjectMapper mapper) {
        this.mqttClient = mqttClient;
        this.mapper = mapper;
    }

    @PostConstruct
    public void start() throws Exception {
        String host = System.getenv("MQTT_HOST");
        String serial = System.getenv("MQTT_SERIAL");
        String code = System.getenv("MQTT_PASSWORD");

        mqttClient.connect(host, serial, code, json -> {
            try {
                latest.set(mapper.readTree(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Optional<JsonNode> getLatestTelemetry() {
        return Optional.ofNullable(latest.get());
    }

    @PreDestroy
    public void stop() throws Exception {
        mqttClient.close();
    }
}
