
package com.project.springbookhub.repository;

        import com.project.springbookhub.model.concretes.Client;
        import com.project.springbookhub.model.enums.Gender;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
        import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

        import java.time.LocalDate;
        import java.util.Optional;

        import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client testClient;

    @BeforeEach
    void setUp() {

        // Arrange
        testClient = Client.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .name("Test")
                .surname("User")
                .birthDay(LocalDate.of(1990, 1, 1))
                .birthPlace("Test City")
                .phoneNumber("1234567890")
                .gender(Gender.MALE)
                .build();
        entityManager.persist(testClient);
    }

    @Test
    @DisplayName("Test if client exists by username")
    void testExistsByUsername() {

        // Act & Assert
        assertThat(clientRepository.existsByUsername(testClient.getUsername())).isTrue();
        assertThat(clientRepository.existsByUsername("nonExistingUser")).isFalse();
    }

    @Test
    @DisplayName("Test if client exists by email")
    void testExistsByEmail() {

        // Act & Assert
        assertThat(clientRepository.existsByEmail(testClient.getEmail())).isTrue();
        assertThat(clientRepository.existsByEmail("nonexisting@example.com")).isFalse();
    }

    @Test
    @DisplayName("Test if client exists by phone number")
    void testExistsByPhoneNumber() {

        // Act & Assert
        assertThat(clientRepository.existsByPhoneNumber(testClient.getPhoneNumber())).isTrue();
        assertThat(clientRepository.existsByPhoneNumber("0000000000")).isFalse();
    }

    @Test
    @DisplayName("Test find client by username")
    void testFindByUsername() {

        // Act & Assert
        Optional<Client> foundClient = clientRepository.findByUsername(testClient.getUsername());
        assertThat(foundClient).isPresent();
        assertThat(foundClient.get().getUsername()).isEqualTo(testClient.getUsername());

        assertThat(clientRepository.findByUsername("nonExistingUser")).isEmpty();
    }
}

