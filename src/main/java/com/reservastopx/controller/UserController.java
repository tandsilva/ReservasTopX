package com.reservastopx.controller;

import com.reservastopx.dto.UserDTO;
import com.reservastopx.enums.Role;
import com.reservastopx.service.UserService;
import com.reservastopx.util.CPFUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Criar usuário
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        // Normaliza CPF (aceita com/sem máscara)
        userDTO.setCpf(onlyDigits(userDTO.getCpf()));

        // Validação simples de CPF (se enviado)
        if (userDTO.getCpf() != null && !CPFUtils.isCPFValid(userDTO.getCpf())) {
            return ResponseEntity.badRequest().body("CPF inválido");
        }

        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity
                    .created(URI.create("/users/" + createdUser.getId()))
                    .body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Buscar todos os usuários
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Buscar todos os admins
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        List<UserDTO> admins = userService.getUsersByRole(Role.ADMIN);
        return ResponseEntity.ok(admins);
    }

    // Buscar todos os usuários comuns
    @GetMapping("/simple")
    public ResponseEntity<List<UserDTO>> getAllSimpleUsers() {
        List<UserDTO> simples = userService.getUsersByRole(Role.USER);
        return ResponseEntity.ok(simples);
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDTO);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Normaliza CPF
        userDTO.setCpf(onlyDigits(userDTO.getCpf()));

        // Valida CPF se informado
        if (userDTO.getCpf() != null && !CPFUtils.isCPFValid(userDTO.getCpf())) {
            return ResponseEntity.badRequest().body("CPF inválido");
        }

        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            if (updatedUser == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    // helper
    private static String onlyDigits(String s) {
        return s == null ? null : s.replaceAll("\\D", "");
    }
}
