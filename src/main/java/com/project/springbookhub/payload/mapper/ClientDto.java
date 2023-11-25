package com.project.springbookhub.payload.mapper;

import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.payload.request.ClientRequest;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.service.RoleService;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class ClientDto {

    private final RoleService roleService;


    public Client mapClientRequestToClient(ClientRequest clientRequest) {
        return Client.builder()
                .username(clientRequest.getUsername())
                .name(clientRequest.getName())
                .surname(clientRequest.getSurname())
                .birthDay(clientRequest.getBirthDay())
                .birthPlace(clientRequest.getBirthPlace())
                .password(clientRequest.getPassword()) // Parolanın hashlenmesi gerekiyor
                .phoneNumber(clientRequest.getPhoneNumber())
                .gender(clientRequest.getGender())
                .email(clientRequest.getEmail())
                .build();
    }

    public ClientResponse mapClientToClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .username(client.getUsername())
                .name(client.getName())
                .surname(client.getSurname())
                .birthDay(client.getBirthDay())
                .birthPlace(client.getBirthPlace())
                .phoneNumber(client.getPhoneNumber())
                .gender(client.getGender())
                .email(client.getEmail())
                .build();
    }



    public static void updateClientFromClientRequest(ClientRequest clientRequest, Client client) {

        client.setUsername(clientRequest.getUsername());
        client.setName(clientRequest.getName());
        client.setSurname(clientRequest.getSurname());
        client.setBirthDay(clientRequest.getBirthDay());
        client.setBirthPlace(clientRequest.getBirthPlace());
        client.setPhoneNumber(clientRequest.getPhoneNumber());
        client.setGender(clientRequest.getGender());
        client.setEmail(clientRequest.getEmail());
        client.setPassword(clientRequest.getPassword());

    }


}

