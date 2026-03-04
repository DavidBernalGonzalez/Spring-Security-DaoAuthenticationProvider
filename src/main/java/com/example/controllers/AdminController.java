package com.example.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/stats")
    public String stats() {
        return "Estadísticas del sistema";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "Usuario " + id + " eliminado";
    }
}