package com.project.springbookhub.payload.mapper;

import com.project.springbookhub.model.concretes.Admin;
import com.project.springbookhub.payload.request.AdminRequest;
import com.project.springbookhub.payload.response.AdminResponse;
import com.project.springbookhub.service.RoleService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AdminDto {

    private final RoleService roleService;


    public static Admin mapAdminRequestToAdmin(AdminRequest adminRequest) {
        return Admin.builder()
                .username(adminRequest.getUsername())
                .name(adminRequest.getName())
                .surname(adminRequest.getSurname())
                .birthDay(adminRequest.getBirthDay())
                .birthPlace(adminRequest.getBirthPlace())
                .password(adminRequest.getPassword()) // Parolanın hashlenmesi gerekiyor
                .phoneNumber(adminRequest.getPhoneNumber())
                .gender(adminRequest.getGender())
                .email(adminRequest.getEmail())
                .build();
    }

    public static AdminResponse mapAdminToAdminResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .surname(admin.getSurname())
                .birthDay(admin.getBirthDay())
                .birthPlace(admin.getBirthPlace())
                .phoneNumber(admin.getPhoneNumber())
                .gender(admin.getGender())
                .email(admin.getEmail())
                .build();
    }



    public static void updateAdminFromAdminRequest(AdminRequest adminRequest, Admin admin) {

        admin.setUsername(adminRequest.getUsername());
        admin.setName(adminRequest.getName());
        admin.setSurname(adminRequest.getSurname());
        admin.setBirthDay(adminRequest.getBirthDay());
        admin.setBirthPlace(adminRequest.getBirthPlace());
        admin.setPhoneNumber(adminRequest.getPhoneNumber());
        admin.setGender(adminRequest.getGender());
        admin.setEmail(adminRequest.getEmail());
        admin.setPassword(adminRequest.getPassword());

    }
}
