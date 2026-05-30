package com.vitral.auth.infraestructure.entry_points;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Vitral Auth funcionando";
    }
}