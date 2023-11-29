package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Admin;
import com.project.springbookhub.model.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
public class AdminRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    private Admin testAdmin;


    @BeforeEach
    void setUp() {
        LocalDate birthDay = LocalDate.of(1990, 1, 1);
        testAdmin = Admin.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .name("Test")
                .surname("User")
                .birthDay(birthDay)
                .birthPlace("Test City")
                .phoneNumber("123456789")
                .gender(Gender.MALE)
                .built_in(false)
                .build();

        entityManager.persist(testAdmin);
    }



    @Test
    void whenFindByUsername_thenReturnAdmin() {
        // Act
        Optional<Admin> found = adminRepository.findByUsername(testAdmin.getUsername());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(testAdmin.getUsername());
    }

 /*
    @BeforeEach
    void setUp() {
        adminRepository = mock(AdminRepository.class);
    }

    @Test
    void whenFindByUsername_thenReturnAdmin() {

        // Arrange
        String username = "testUser";
        Admin admin = new Admin();
        admin.setUsername(username);
        when(adminRepository.findByUsername(username)).thenReturn(Optional.of(admin));

        // Act
        Optional<Admin> found = adminRepository.findByUsername(username);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(username);

    }*/

}
