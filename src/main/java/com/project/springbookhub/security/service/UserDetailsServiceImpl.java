package com.project.springbookhub.security.service;

import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Data
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

        private final ClientRepository clientRepository;

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Client client = clientRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

            if (client != null) {
                return new UserDetailsImpl(
                        client.getId(),
                        client.getUsername(),
                        client.getName(),
                        false,
                        client.getPassword(),
                        client.getRole().getRoleType().name());
            }
            throw new UsernameNotFoundException("User '" + username+ "  ' not found");

        }
}

