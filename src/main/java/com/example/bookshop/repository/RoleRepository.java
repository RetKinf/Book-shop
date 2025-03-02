package com.example.bookshop.repository;

import com.example.bookshop.model.Role;
import com.example.bookshop.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
