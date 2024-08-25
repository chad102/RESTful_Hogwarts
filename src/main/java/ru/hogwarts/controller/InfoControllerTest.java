package ru.hogwarts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class InfoControllerTest extends InfoController {
    @Autowired
    Environment environment;

    @GetMapping("/port")
    @Override
    public ResponseEntity<String> getTestPort() {
        String port = environment.getProperty("server.port");
        return ResponseEntity.ok(port);
    }
}
