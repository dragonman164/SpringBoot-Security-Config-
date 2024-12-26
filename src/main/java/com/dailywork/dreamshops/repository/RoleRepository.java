package com.dailywork.dreamshops.repository;

import com.dailywork.dreamshops.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findByName(String role);
}
