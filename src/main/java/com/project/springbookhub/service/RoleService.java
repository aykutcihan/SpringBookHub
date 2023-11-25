package com.project.springbookhub.service;


import com.project.springbookhub.exception.ConflictException;
import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.RoleType;
import com.project.springbookhub.repository.RoleRepository;
import com.project.springbookhub.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getUserRole(RoleType roleType){
        return roleRepository.findByEnumRoleEquals(roleType).orElseThrow(
                ()-> new ConflictException(Messages.ROLE_NOT_FOUND)
        );
    }

    public List<Role> getUserRole(){
        return  roleRepository.findAll();
    }

    public Role save(RoleType roleType){
        if (roleRepository.existsByEnumRoleEquals(roleType)){
            throw new ConflictException(Messages.ROLE_ALREADY_EXIST);
        }

        Role userRole = Role.builder()
                .roleType(roleType)
                .build();
        return roleRepository.save(userRole);
    }
}
