package com.project.springbookhub.service;

import com.project.springbookhub.exception.UserNotFoundException;
import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.Gender;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.ClientDto;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.repository.ClientRepository;
import com.project.springbookhub.util.Messages;
import com.project.springbookhub.util.ServiceHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ServiceHelpers serviceHelpers;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientDto clientDto;

    @InjectMocks
    private ClientService clientService;

    private ClientRequest clientRequest;
    private Client client;
    private ClientResponse clientResponse;

    @BeforeEach
    void setUp() {
        clientRequest = ClientRequest.builder()
                .username("testUser")
                .email("test@example.com")
                .password("Password123!")
                .name("Test")
                .surname("User")
                .birthDay(LocalDate.of(1990, 1, 1))
                .birthPlace("City")
                .phoneNumber("123-456-7890")
                .gender(Gender.MALE)
                .build();

        client = Client.builder()
                .id(1L)
                .username(clientRequest.getUsername())
                .email(clientRequest.getEmail())
                .password(clientRequest.getPassword()) // Gerçekte bu şifrelenmiş olacaktır
                .name(clientRequest.getName())
                .surname(clientRequest.getSurname())
                .birthDay(clientRequest.getBirthDay())
                .birthPlace(clientRequest.getBirthPlace())
                .phoneNumber(clientRequest.getPhoneNumber())
                .gender(clientRequest.getGender())
                .build();

        clientResponse = ClientResponse.builder()
                .id(1L)
                .username(client.getUsername())
                .email(client.getEmail())
                .name(client.getName())
                .surname(client.getSurname())
                .birthDay(client.getBirthDay())
                .birthPlace(client.getBirthPlace())
                .phoneNumber(client.getPhoneNumber())
                .gender(client.getGender())
                .build();
    }

    @Test
    void registerClientTest() {

        // Arrange
        when(clientDto.mapClientRequestToClient(clientRequest)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(clientDto.mapClientToClientResponse(any(Client.class))).thenReturn(clientResponse);
        when(roleService.getUserRole(RoleType.ROLE_CLIENT)).thenReturn(new Role()); // Rol ataması için mock
        when(passwordEncoder.encode(clientRequest.getPassword())).thenReturn("encodedPassword");

        // Act
        ResponseMessage<ClientResponse> response = clientService.registerClient(clientRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(String.format(Messages.USER_SAVED, "client"), response.getMessage());
        assertEquals(clientResponse, response.getObject());

        // Verify
        verify(clientRepository).save(any(Client.class));
        verify(clientDto).mapClientRequestToClient(clientRequest);
        verify(clientDto).mapClientToClientResponse(any(Client.class));
        verify(roleService).getUserRole(RoleType.ROLE_CLIENT);
        verify(passwordEncoder).encode(clientRequest.getPassword());
    }


    @Test
    void authenticateClientTest() {
        // Arrange
        String encodedPassword = passwordEncoder.encode("Password123!");
        client.setPassword(encodedPassword);

        when(clientRepository.findByUsername(clientRequest.getUsername())).thenReturn(Optional.of(client));
        when(passwordEncoder.matches(clientRequest.getPassword(), client.getPassword())).thenReturn(true);
        when(clientDto.mapClientToClientResponse(client)).thenReturn(clientResponse);

        // Act
        ResponseMessage<ClientResponse> response = clientService.authenticateClient(clientRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(String.format(Messages.USER_AUTHENTICATED, client), response.getMessage());
        assertEquals(clientResponse, response.getObject());

        // Verify
        verify(clientRepository).findByUsername(clientRequest.getUsername());
        verify(passwordEncoder).matches(clientRequest.getPassword(), client.getPassword());
        verify(clientDto).mapClientToClientResponse(client);
    }


    @Test
    void getClientProfileTest() {
        // Arrange
        String username = "testUser";
        when(clientRepository.findByUsername(username)).thenReturn(Optional.of(client));
        when(clientDto.mapClientToClientResponse(client)).thenReturn(clientResponse);

        // Act
        ResponseMessage<ClientResponse> response = clientService.getClientProfile(username);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(" User profile retrieved successfully", response.getMessage());
        assertEquals(clientResponse, response.getObject());

        // Verify
        verify(clientRepository).findByUsername(username);
        verify(clientDto).mapClientToClientResponse(client);
    }

    @Test
    void getClientProfileNotFoundTest() {
        // Arrange
        String username = "nonExistingUser";
        when(clientRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> clientService.getClientProfile(username));

        // Verify
        verify(clientRepository).findByUsername(username);
    }


    @Test
    void allClientProfileTest() {
        // Arrange
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientDto.mapClientToClientResponse(any(Client.class))).thenReturn(clientResponse);

        // Act
        List<ClientResponse> responses = clientService.allClientProfile();

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(clients.size(), responses.size());
        assertEquals(clientResponse, responses.get(0));

        // Verify
        verify(clientRepository).findAll();
        verify(clientDto, times(clients.size())).mapClientToClientResponse(any(Client.class));
    }


    @Test
    void updateClientProfileTest() {
        // Arrange
        Long clientId = 1L;
        client.setId(clientId); // ID'yi ayarla
        ClientRequest updatedRequest = ClientRequest.builder()
                .username("updatedUser")
                .email("updated@example.com")
                .password("UpdatedPassword123!")
                .name("Updated")
                .surname("User")
                .birthDay(LocalDate.of(1991, 2, 2))
                .birthPlace("UpdatedCity")
                .phoneNumber("987-654-3210")
                .gender(Gender.FEMALE)
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client); // Güncellenmiş client'ı döndür
        when(clientDto.mapClientToClientResponse(any(Client.class))).thenReturn(clientResponse);
        ClientDto.updateClientFromClientRequest(updatedRequest, client); // Static metodun etkisi için mocklama yok

        // Act
        ResponseMessage<ClientResponse> response = clientService.updateClientProfile(clientId, updatedRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("User updated successfully", response.getMessage());
        assertEquals(clientResponse, response.getObject());

        // Verify
        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(client);
        verify(clientDto).mapClientToClientResponse(client);
    }

    @Test
    void updateClientProfileNotFoundTest() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            clientService.updateClientProfile(clientId, clientRequest);
        });

        // Verify
        verify(clientRepository).findById(clientId);
    }


    @Test
    void deleteClientTest() {
        Long clientId = client.getId();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).deleteById(clientId);

        ResponseMessage<?> response = clientService.deleteClient(clientId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("User deleted successfully", response.getMessage());

        verify(clientRepository).findById(clientId);
        verify(clientRepository).deleteById(clientId);
    }





    @Test
    void deleteClientNotFoundTest() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            clientService.deleteClient(clientId);
        });

        // Verify
        verify(clientRepository).findById(clientId);
    }

}
