package com.example.musiccollection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.musiccollection.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
     public Role findByRoleName(String RoleName);
}
