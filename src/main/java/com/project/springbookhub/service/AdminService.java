package com.project.springbookhub.service;

import com.project.springbookhub.exception.ConflictException;
import com.project.springbookhub.exception.UserNotFoundException;
import com.project.springbookhub.model.concretes.Admin;
import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.ClientDto;
import com.project.springbookhub.payload.mapper.AdminDto;
import com.project.springbookhub.payload.request.AdminRequest;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.AdminResponse;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.repository.AdminRepository;
import com.project.springbookhub.util.Messages;
import com.project.springbookhub.util.ServiceHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ServiceHelpers serviceHelpers;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private final AdminDto adminDto;


    public ResponseMessage<AdminResponse> registerAdmin(AdminRequest adminRequest) {
        serviceHelpers.checkDuplicate(adminRequest.getUsername(), adminRequest.getEmail(), adminRequest.getPhoneNumber());

        Admin admin= adminDto.mapAdminRequestToAdmin(adminRequest);

        admin.setBuilt_in(false);

        //if username is also Admin we are setting built_in prop. to FALSE
        if(Objects.equals(adminRequest.getUsername(),"Admin")){
            admin.setBuilt_in(true);
        }
        admin.setRole(roleService.getUserRole(RoleType.ROLE_ADMIN));

        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));


        return ResponseMessage.<AdminResponse>builder()
                .message(String.format(Messages.USER_SAVED, "admin"))
                .object(adminDto.mapAdminToAdminResponse(adminRepository.save(admin)))
                .httpStatus(HttpStatus.CREATED)
                .build();

    }
    public ResponseMessage<AdminResponse> authenticateAdmin(AdminRequest adminRequest) {
        Admin admin = adminRepository.findByUsername(adminRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USERNAME_NOT_FOUND,
                        adminRequest.getUsername())));

        if (!passwordEncoder.matches(adminRequest.getPassword(), admin.getPassword())) {
            throw new BadCredentialsException(String.format(Messages.INVALID_PASSWORD));
        }
        return ResponseMessage.<AdminResponse>builder()
                .message(String.format(Messages.USER_AUTHENTICATED, admin))
                .object(adminDto.mapAdminToAdminResponse(admin))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<AdminResponse> getAdminProfile(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USERNAME_NOT_FOUND, username)));


        AdminResponse adminResponse = adminDto.mapAdminToAdminResponse(admin);
        return ResponseMessage.<AdminResponse>builder()
                .object(adminResponse)
                .message(" User profile retrieved successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public List<AdminResponse> allAdminProfile() {

        List<Admin> clients = adminRepository.findAll();
        return clients.stream()
                .map(adminDto::mapAdminToAdminResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<AdminResponse> updateAdminProfile(Long id, AdminRequest adminRequest) {
        Admin admin= adminRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USER_NOT_FOUND, id)));

        adminDto.updateAdminFromAdminRequest(adminRequest, admin);
        AdminResponse adminResponse = adminDto.mapAdminToAdminResponse(adminRepository.save(admin));;

        return ResponseMessage.<AdminResponse>builder()
                .object(adminResponse)
                .message("User updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public String deleteAdmin(Long id){
        Optional<Admin> admin = adminRepository.findById(id);
        if(admin.isPresent() && admin.get().isBuilt_in()){
            throw new ConflictException(Messages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if (admin.isPresent()){
            adminRepository.deleteById(id);
            return Messages.ADMIN_DELETED_SUCCESSFULLY;
        }

        return String.format(Messages.NOT_FOUND_USER_MESSAGE,id);

    }
    public long countAllAdmins(){
        return adminRepository.count();
    }

}
