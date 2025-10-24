package com.reservastopx.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// AdminController
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/ping") // público pra testar mapeamento do /admin
    public String ping() { return "admin-ok"; }

    @GetMapping("/painel")
    @PreAuthorize("hasRole('ADMIN')")
    public String painelAdmin() { return "Bem-vindo ao painel admin!"; }
}
