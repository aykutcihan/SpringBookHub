package com.project.springbookhub.util;

import com.project.springbookhub.exception.ConflictException;
import com.project.springbookhub.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ServiceHelpers {

    private final ClientRepository clientRepository;

    public void checkDuplicate(String... values) {

        String username = values[0];
        String email = values[1];
        String phoneNumber = "";

        if (values.length == 3) {
            phoneNumber = values[2];
        }

        if (clientRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
        }
        if (clientRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }
        if (!phoneNumber.isEmpty() && clientRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE, phoneNumber));
        }

    }

    public Pageable getPageableWithProperties(int page, int size, String sort, String type){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());
        if(Objects.equals(type,"desc")){
            pageable = PageRequest.of(page,size, Sort.by(sort).descending());
        }
        return pageable;
    }

}
