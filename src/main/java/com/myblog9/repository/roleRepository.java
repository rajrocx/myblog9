package com.myblog9.repository;

import com.myblog9.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface roleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

