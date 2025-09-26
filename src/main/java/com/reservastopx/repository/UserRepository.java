package com.reservastopx.repository;

import com.reservastopx.enums.Role;
import com.reservastopx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    List<User> findByRole(Role role); // novo método
}
