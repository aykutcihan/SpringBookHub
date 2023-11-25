package com.project.springbookhub.config;


import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.Gender;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.payload.request.AdminRequest;
import com.project.springbookhub.repository.AdminRepository;
import com.project.springbookhub.repository.RoleRepository;
import com.project.springbookhub.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class SetupDataLoader {

    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.surname}")
    private String adminSurname;

    @Value("${app.admin.phoneNumber}")
    private String adminPhoneNumber;

    @Value("${app.admin.birthDay}")
    private String adminBirthDay;

    @Value("${app.admin.birthPlace}")
    private String adminBirthPlace;

    @Value("${app.admin.gender}")
    private String adminGender;

    public SetupDataLoader(RoleRepository roleRepository, AdminRepository adminRepository, AdminService adminService, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    CommandLineRunner initDatabase () {
        return args -> {
            if (!roleRepository.existsByEnumRoleEquals(RoleType.ROLE_CLIENT)) {
                Role userRole = new Role(null, RoleType.ROLE_CLIENT);
                roleRepository.save(userRole);
            }

            if (!roleRepository.existsByEnumRoleEquals(RoleType.ROLE_ADMIN)) {
                Role adminRole = new Role(null, RoleType.ROLE_ADMIN);
                roleRepository.save(adminRole);
            }
            if(adminService.countAllAdmins()==0){
                AdminRequest adminRequest  = new AdminRequest();
                adminRequest.setUsername(adminUsername);
                adminRequest.setEmail(adminEmail);
                adminRequest.setPassword(passwordEncoder.encode(adminPassword));
                adminRequest.setName(adminName);
                adminRequest.setSurname(adminSurname);
                adminRequest.setPhoneNumber(adminPhoneNumber);
                adminRequest.setBirthDay(LocalDate.parse(adminBirthDay));
                adminRequest.setBirthPlace(adminBirthPlace);
                adminRequest.setGender(Gender.valueOf(adminGender.toUpperCase()));
                adminService.registerAdmin(adminRequest);
            }
        };
    }
}
