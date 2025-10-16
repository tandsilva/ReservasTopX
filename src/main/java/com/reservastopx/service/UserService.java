package com.reservastopx.service;

import com.reservastopx.dto.UserDTO;
import com.reservastopx.enums.Role;
import com.reservastopx.mapper.UserMapper;
import com.reservastopx.model.User;
import com.reservastopx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username já existe");
        }

        if(userRepository.existsByCpf(userDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        validatePassword(userDTO.getPassword());
        // Verifica se role é nula, define default como USER
        if(userDTO.getRole() == null) {
            userDTO.setRole(Role.USER);
        }

        User user = UserMapper.toEntity(userDTO);
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }


    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElse(null);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDTO.getUsername());

            // ✅ Valida senha apenas se ela for informada (pra não obrigar atualizar senha sempre)
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                validatePassword(userDTO.getPassword());
                user.setPassword(userDTO.getPassword());
            }

            user.setRole(userDTO.getRole());

            User updated = userRepository.save(user);
            return UserMapper.toDTO(updated);
        }).orElse(null);
    }


    public boolean deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
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
