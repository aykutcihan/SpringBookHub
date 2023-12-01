package com.project.springbookhub.service;

import com.project.springbookhub.exception.ConflictException;
import com.project.springbookhub.exception.UserNotFoundException;
import com.project.springbookhub.model.concretes.Admin;
import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.Gender;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.AdminDto;
import com.project.springbookhub.payload.request.AdminRequest;
import com.project.springbookhub.payload.response.AdminResponse;
import com.project.springbookhub.repository.AdminRepository;
import com.project.springbookhub.util.Messages;
import com.project.springbookhub.util.ServiceHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.trim;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ServiceHelpers serviceHelpers;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminDto adminDto;

    @InjectMocks
    private AdminService adminService;

    private AdminRequest adminRequest;
    private Admin admin;
    private AdminResponse adminResponse;

    @BeforeEach
    void setUp() {
        adminRequest = AdminRequest.builder()
                .username("adminUser")
                .email("admin@example.com")
                .password("Admin123!")
                .name("Admin")
                .surname("User")
                .birthDay(LocalDate.of(1990, 1, 1))
                .birthPlace("City")
                .phoneNumber("123-456-7890")
                .gender(Gender.MALE)
                .build();

        admin = Admin.builder()
                .id(1L)
                .username(adminRequest.getUsername())
                .email(adminRequest.getEmail())
                .password(adminRequest.getPassword()) // Gerçekte bu şifrelenmiş olacaktır
                .name(adminRequest.getName())
                .surname(adminRequest.getSurname())
                .birthDay(adminRequest.getBirthDay())
                .birthPlace(adminRequest.getBirthPlace())
                .phoneNumber(adminRequest.getPhoneNumber())
                .gender(adminRequest.getGender())
                .built_in(false)
                .build();

        adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .name(admin.getName())
                .surname(admin.getSurname())
                .birthDay(admin.getBirthDay())
                .birthPlace(admin.getBirthPlace())
                .phoneNumber(admin.getPhoneNumber())
                .gender(admin.getGender())
                .build();
    }

    @Test
    void registerAdminTest() {

        // Arrange
        doNothing().when(serviceHelpers).checkDuplicate( // doNothing(): don't throw any exceptions
                adminRequest.getUsername(), adminRequest.getEmail(), adminRequest.getPhoneNumber());
        when(adminDto.mapAdminRequestToAdmin(adminRequest)).thenReturn(admin);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        when(adminDto.mapAdminToAdminResponse(admin)).thenReturn(adminResponse);

        // Act
        ResponseMessage<AdminResponse> response = adminService.registerAdmin(adminRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(adminResponse, response.getObject());
        assertEquals(String.format(Messages.USER_SAVED, "admin"), response.getMessage());

        // Additional verification to ensure the correct methods were called with expected parameters
        verify(serviceHelpers).checkDuplicate(adminRequest.getUsername(), adminRequest.getEmail(), adminRequest.getPhoneNumber());
        verify(adminRepository).save(admin);
    }

    @Test
    void authenticateAdminTest() {

        // Arrange
        when(adminRepository.findByUsername(adminRequest.getUsername()))
                .thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(adminRequest.getPassword(), admin.getPassword()))
                .thenReturn(true);
        when(adminDto.mapAdminToAdminResponse(admin)).thenReturn(adminResponse);

        // Act
        ResponseMessage<AdminResponse> response = adminService.authenticateAdmin(adminRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(adminResponse, response.getObject());
        assertEquals(String.format(Messages.USER_AUTHENTICATED, admin), response.getMessage());

        // Verify the interactions
        verify(adminRepository).findByUsername(adminRequest.getUsername());
        verify(passwordEncoder).matches(adminRequest.getPassword(), admin.getPassword());
    }


    @Test
    void getAdminProfileTest() {
        // Arrange
        when(adminRepository.findByUsername(adminRequest.getUsername()))
                .thenReturn(Optional.of(admin));
        when(adminDto.mapAdminToAdminResponse(admin)).thenReturn(adminResponse);

        // Act
        ResponseMessage<AdminResponse> response = adminService.getAdminProfile(adminRequest.getUsername());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(adminResponse, response.getObject());
        assertEquals(" User profile retrieved successfully", response.getMessage());

        // Verify the interactions
        verify(adminRepository).findByUsername(adminRequest.getUsername());
    }

    @Test
    void getAdminProfileNotFoundTest() {
        // Arrange
        when(adminRepository.findByUsername(adminRequest.getUsername()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            adminService.getAdminProfile(adminRequest.getUsername());
        });

        // Verify the interaction
        verify(adminRepository).findByUsername(adminRequest.getUsername());
    }

    @Test
    void allAdminProfileTest() {
        // Arrange
        List<Admin> admins = Arrays.asList(admin); // Admin listesi oluştur
        when(adminRepository.findAll()).thenReturn(admins);
        when(adminDto.mapAdminToAdminResponse(any(Admin.class))).thenReturn(adminResponse);

        // Act
        List<AdminResponse> response = adminService.allAdminProfile();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(admins.size(), response.size());
        assertEquals(adminResponse, response.get(0));

        // Verify the interactions
        verify(adminRepository).findAll();
        verify(adminDto, times(admins.size())).mapAdminToAdminResponse(any(Admin.class));
    }

    @Test
    void updateAdminProfileTest() {
        // Arrange
        Long adminId = admin.getId();
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        when(adminDto.mapAdminToAdminResponse(admin)).thenReturn(adminResponse);

        // Act
        ResponseMessage<AdminResponse> response = adminService.updateAdminProfile(adminId, adminRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(adminResponse, response.getObject());
        assertEquals("User updated successfully", response.getMessage());

        // Verify the interactions
        verify(adminRepository).findById(adminId);
        verify(adminRepository).save(admin);
        verify(adminDto).mapAdminToAdminResponse(admin);
    }

    @Test
    void updateAdminProfileNotFoundTest() {
        // Arrange
        Long adminId = admin.getId();
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            adminService.updateAdminProfile(adminId, adminRequest);
        });

        // Verify the interaction
        verify(adminRepository).findById(adminId);
    }

    @Test
    void deleteAdminTest() {
        // Arrange
        Long adminId = admin.getId();
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        doNothing().when(adminRepository).deleteById(adminId);

        // Act
        String result = adminService.deleteAdmin(adminId);

        // Assert
        assertEquals(Messages.ADMIN_DELETED_SUCCESSFULLY, result);

        // Verify the interactions
        verify(adminRepository).findById(adminId);
        verify(adminRepository).deleteById(adminId);
    }

    @Test
    void deleteAdminNotFoundTest() {
        // Arrange
        Long adminId = admin.getId();
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        String result = adminService.deleteAdmin(adminId);

        // Assert
        assertEquals(String.format(Messages.NOT_FOUND_USER_MESSAGE, adminId), result);

        // Verify the interaction
        verify(adminRepository).findById(adminId);
    }

    @Test
    void deleteAdminNotPermittedTest() {
        // Arrange
        Long adminId = admin.getId();
        admin.setBuilt_in(true); // Set built_in to true to simulate non-deletable admin
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // Act & Assert
        assertThrows(ConflictException.class, () -> {
            adminService.deleteAdmin(adminId);
        });

        // Verify the interaction
        verify(adminRepository).findById(adminId);
    }

}
