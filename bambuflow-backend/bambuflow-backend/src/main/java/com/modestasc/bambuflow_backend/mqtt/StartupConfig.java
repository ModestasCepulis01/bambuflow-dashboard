package com.modestasc.bambuflow_backend.mqtt;

import com.modestasc.bambuflow_backend.mqtt.BambuMqttClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    CommandLineRunner startPrinter() {
        return args -> {
            BambuMqttClient client = new BambuMqttClient();

            client.connect(
                    "192.168.0.13",      // printer IP
                    "00M00A2A1400766",    // serial
                    "1f9928f8"            // access code
            );
        };
    }
}
