package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r FROM Role r WHERE r.roleType = ?1")
    Optional<Role> findByEnumRoleEquals(RoleType roleType);

    @Query("SELECT (count (r)>0) FROM Role r WHERE r.roleType = ?1")
    boolean existsByEnumRoleEquals(RoleType roleType);
}
