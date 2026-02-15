package com.modestasc.bambuflow_backend.mqtt.IT;

import com.modestasc.bambuflow_backend.mqtt.BambuMqttClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BambuMqttClientIT {

    @Test
    void connectsToRealPrinter() throws Exception {
        String host = System.getenv("BAMBU_HOST");
        String serial = System.getenv("BAMBU_SERIAL");
        String code = System.getenv("BAMBU_ACCESS_CODE");

        assertNotNull(host, "Missing env var BAMBU_HOST");
        assertNotNull(serial, "Missing env var BAMBU_SERIAL");
        assertNotNull(code, "Missing env var BAMBU_ACCESS_CODE");

        BambuMqttClient client = new BambuMqttClient();

        assertDoesNotThrow(() -> {
            client.connect(host, serial, code);
            client.close();
        });
    }
}