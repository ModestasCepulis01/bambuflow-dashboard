package com.modestasc.bambuflow_backend.mqtt.api;

import com.modestasc.bambuflow_backend.mqtt.PrinterStateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/printer")
public class PrinterController {

    private final PrinterStateService state;

    public PrinterController(PrinterStateService state) {
        this.state = state;
    }

    @GetMapping("/state")
    public ResponseEntity<?> getState() {
        return state.getLatestTelemetry()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(503).body("No telemetry yet"));
    }
}
