package com.reservastopx.service;

import com.reservastopx.dto.UserDTO;
import com.reservastopx.enums.Role;
import com.reservastopx.mapper.UserMapper;
import com.reservastopx.model.User;
import com.reservastopx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder; // mantém o hash no backend

    public UserDTO createUser(UserDTO userDTO) {
        // Role padrão
        if (userDTO.getRole() == null) userDTO.setRole(Role.USER);

        // Normaliza CPF (aceita com/sem máscara)
        userDTO.setCpf(onlyDigits(userDTO.getCpf()));

        // Unicidade
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username já existe");
        }
        if (userDTO.getCpf() != null && userRepository.existsByCpf(userDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        // Senha
        validatePassword(userDTO.getPassword());
        String hash = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(hash); // o mapper salva já hasheado

        // Mapear e salvar
        User entity = UserMapper.toEntity(userDTO);
        entity.setPassword(hash);

        User saved = userRepository.save(entity);
        return UserMapper.toDTO(saved);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElse(null);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        // Normaliza CPF
        userDTO.setCpf(onlyDigits(userDTO.getCpf()));

        return userRepository.findById(id).map(user -> {
            // Username (permite manter o próprio)
            if (userDTO.getUsername() != null &&
                    !user.getUsername().equals(userDTO.getUsername()) &&
                    userRepository.existsByUsername(userDTO.getUsername())) {
                throw new IllegalArgumentException("Username já existe");
            }
            if (userDTO.getUsername() != null) user.setUsername(userDTO.getUsername());

            // Senha (só se informada)
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                validatePassword(userDTO.getPassword());
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            // Role (mantém a atual se vier nulo)
            if (userDTO.getRole() != null) user.setRole(userDTO.getRole());

            // CPF (opcional para qualquer role; valida unicidade)
            if (userDTO.getCpf() != null && !userDTO.getCpf().equals(user.getCpf())) {
                if (userRepository.existsByCpf(userDTO.getCpf())) {
                    throw new IllegalArgumentException("CPF já cadastrado");
                }
                user.setCpf(userDTO.getCpf());
            }

            // Campos opcionais
            if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());
            if (userDTO.getTelefone() != null) user.setTelefone(userDTO.getTelefone());

            User updated = userRepository.save(user);
            return UserMapper.toDTO(updated);
        }).orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // helpers
    private static String onlyDigits(String s) {
        return s == null ? null : s.replaceAll("\\D", "");
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 10) {
            throw new IllegalArgumentException("Senha deve ter ao menos 10 caracteres");
        }
        if (!rawPassword.matches(".*[A-Z].*") ||
                !rawPassword.matches(".*[a-z].*") ||
                !rawPassword.matches(".*\\d.*") ||
                !rawPassword.matches(".*[^A-Za-z0-9].*")) {
            throw new IllegalArgumentException("Senha deve conter letras maiúsculas, minúsculas, números e símbolo");
        }
    }
}
