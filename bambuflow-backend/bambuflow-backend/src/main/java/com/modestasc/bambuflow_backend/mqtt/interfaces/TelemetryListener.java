package com.modestasc.bambuflow_backend.mqtt.interfaces;

@FunctionalInterface
public interface TelemetryListener {
    void onTelemetryJson(String json);
}
