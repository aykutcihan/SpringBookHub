package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Arrange
        Role adminRole = Role.builder()
                .roleType(RoleType.ROLE_ADMIN)
                .build();
        Role clientRole = Role.builder()
                .roleType(RoleType.ROLE_CLIENT)
                .build();

        entityManager.persist(adminRole); // save
        entityManager.persist(clientRole);
    }

    @Test
    void whenFindByEnumRoleEqualsAdmin_thenRoleShouldBeFound() {
        Optional<Role> foundRole = roleRepository.findByEnumRoleEquals(RoleType.ROLE_ADMIN);
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getRoleType()).isEqualTo(RoleType.ROLE_ADMIN);
    }

    @Test
    void whenFindByEnumRoleEqualsClient_thenRoleShouldBeFound() {
        Optional<Role> foundRole = roleRepository.findByEnumRoleEquals(RoleType.ROLE_CLIENT);
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getRoleType()).isEqualTo(RoleType.ROLE_CLIENT);
    }

    @Test
    void whenExistsByEnumRoleEqualsAdmin_thenShouldReturnTrue() {
        boolean exists = roleRepository.existsByEnumRoleEquals(RoleType.ROLE_ADMIN);
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEnumRoleEqualsClient_thenShouldReturnTrue() {
        boolean exists = roleRepository.existsByEnumRoleEquals(RoleType.ROLE_CLIENT);
        assertThat(exists).isTrue();
    }
}
