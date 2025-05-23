package team28.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class TestConnection {
    @GetMapping
    public Map<String, String> getStatus() {
        return Map.of("message", "Backend is running VERSION 15:20");
    }
}
