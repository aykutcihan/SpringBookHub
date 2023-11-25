package com.project.springbookhub.controller;

import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.AdminRequest;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.AdminResponse;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.service.AdminService;
import com.project.springbookhub.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/signup")
    public ResponseMessage<AdminResponse> registerAdmin(@RequestBody @Valid AdminRequest adminRequest) {
        return adminService.registerAdmin(adminRequest);
    }

    @PostMapping("/signin")
    public ResponseMessage<AdminResponse> authenticateAdmin(@RequestBody AdminRequest adminRequest) {
        return adminService.authenticateAdmin(adminRequest);

    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseMessage<AdminResponse> getAdminProfile(@PathVariable String username) {
        return adminService.getAdminProfile(username);
    }

    @GetMapping("/allProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AdminResponse> allAdminProfile() {
        return adminService.allAdminProfile();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseMessage<AdminResponse> updateAdminProfile(@PathVariable Long id,
                                                             @RequestBody @Valid AdminRequest adminRequest) {
        return adminService.updateAdminProfile(id, adminRequest);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteAdmin(id));
    }
}
