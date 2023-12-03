package com.project.springbookhub.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;


    @Test
    void sendSimpleMessageTest() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test message content";

        // Act
        emailService.sendSimpleMessage(to, subject, text);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
