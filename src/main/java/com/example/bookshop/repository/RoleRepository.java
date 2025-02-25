package com.example.bookshop.repository;

import com.example.bookshop.model.Role;
import com.example.bookshop.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(RoleName name);
}
