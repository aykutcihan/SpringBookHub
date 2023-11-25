package com.project.springbookhub.controller;

import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/signup")
    public ResponseMessage<ClientResponse> registerClient(@RequestBody @Valid ClientRequest clientRequest) {
        return clientService.registerClient(clientRequest);
    }
    @PostMapping("/signin")
    public ResponseMessage<ClientResponse> authenticateUser(@RequestBody ClientRequest clientRequest) {
        return clientService.authenticateClient(clientRequest);

    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseMessage<ClientResponse> getClientProfile(@PathVariable String username) {
        return clientService.getClientProfile(username);
    }

    @GetMapping("/allProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ClientResponse> allUserProfile() {
        return clientService.allClientProfile();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseMessage<ClientResponse> updateUserProfile(@PathVariable Long id,
                                                             @RequestBody @Valid ClientRequest clientRequest) {
        return clientService.updateClientProfile(id, clientRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseMessage<?> deleteClient (@PathVariable Long id) {
        return clientService.deleteClient(id);
    }


}
