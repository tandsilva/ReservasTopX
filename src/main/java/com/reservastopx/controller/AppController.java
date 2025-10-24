package com.reservastopx.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// AppController
@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/ping") // público pra testar mapeamento
    public String ping() { return "app-ok"; }

    @GetMapping("/mostraUser")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Object me(org.springframework.security.core.Authentication a){
        return a == null ? "no-auth" : a.getAuthorities();
    }

    @GetMapping("/perfil")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String perfilUsuario() { return "Dados básicos do usuário"; }

    @GetMapping("/reservas")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String minhasReservas() { return "Listagem de reservas do usuário logado"; }

    @PostMapping("/reservas")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String criarReserva() { return "Reserva criada com sucesso!"; }
}
