package com.project.springbookhub.service;

import com.project.springbookhub.exception.ConflictException;
import com.project.springbookhub.exception.UserNotFoundException;
import com.project.springbookhub.model.concretes.Admin;
import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.ClientDto;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.repository.ClientRepository;
import com.project.springbookhub.util.Messages;
import com.project.springbookhub.util.ServiceHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ServiceHelpers serviceHelpers;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private final ClientDto clientDto;
    public ResponseMessage<ClientResponse> registerClient(ClientRequest clientRequest) {
        serviceHelpers.checkDuplicate(clientRequest.getUsername(), clientRequest.getEmail(), clientRequest.getPhoneNumber());

        Client client = clientDto.mapClientRequestToClient(clientRequest);
        client.setRole(roleService.getUserRole(RoleType.ROLE_CLIENT));
        client.setPassword(passwordEncoder.encode(clientRequest.getPassword()));


        return ResponseMessage.<ClientResponse>builder()
                .message(String.format(Messages.USER_SAVED, "client"))
                .object(clientDto.mapClientToClientResponse(clientRepository.save(client)))
                .httpStatus(HttpStatus.CREATED)
                .build();

    }
    public ResponseMessage<ClientResponse> authenticateClient(ClientRequest clientRequest) {
        Client client = clientRepository.findByUsername(clientRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USERNAME_NOT_FOUND,
                        clientRequest.getUsername())));

        if (!passwordEncoder.matches(clientRequest.getPassword(), client.getPassword())) {
            throw new BadCredentialsException(String.format(Messages.INVALID_PASSWORD));
        }
        return ResponseMessage.<ClientResponse>builder()
                .message(String.format(Messages.USER_AUTHENTICATED, client))
                .object(clientDto.mapClientToClientResponse(client))
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<ClientResponse> getClientProfile(String username) {
        Client client = clientRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USERNAME_NOT_FOUND, username)));


        ClientResponse clientResponse = clientDto.mapClientToClientResponse(client);
        return ResponseMessage.<ClientResponse>builder()
                .object(clientResponse)
                .message(" User profile retrieved successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public List<ClientResponse> allClientProfile() {

        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(clientDto::mapClientToClientResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<ClientResponse> updateClientProfile(Long id, ClientRequest clientRequest) {
        Client client= clientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USER_NOT_FOUND, id)));

        ClientDto.updateClientFromClientRequest(clientRequest, client);
        ClientResponse clientResponse = clientDto.mapClientToClientResponse(clientRepository.save(client));;

        return ResponseMessage.<ClientResponse>builder()
                .object(clientResponse)
                .message("User updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<?> deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(Messages.USER_NOT_FOUND, id)));

        clientRepository.deleteById(client.getId());
        return ResponseMessage.builder()
                .message("User deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }



}

