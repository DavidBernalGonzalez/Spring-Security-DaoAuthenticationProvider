package com.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public String profile() {
        return "Perfil usuario";
    }

    @GetMapping("/tasks")
    public String myTasks() {
        return "Lista de mis tareas";
    }

    @PostMapping("/tasks")
    public String createTask() {
        return "Tarea creada";
    }
}