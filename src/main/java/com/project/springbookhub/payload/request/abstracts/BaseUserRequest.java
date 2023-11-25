package com.project.springbookhub.payload.request.abstracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.springbookhub.model.concretes.Role;
import com.project.springbookhub.model.enums.Gender;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@SuperBuilder
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUserRequest {

    @NotNull(message = "Please enter your username")
    @Size(min = 4, max = 16,message = "Your username should be at least {min} chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your username must consist of the characters .")
    private String username;

    @NotNull(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Size(min=5, max=50 , message = "Your email should be between {min} and {max} chars")
    private String email;

    @NotNull(message = "Please enter your password")
    @Size(min = 8, max = 60,message = "Your password should be at least {min} chars or maximum {max} characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-+_!@#$%^&*., ?]).+$",
            message = "Your password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character (-+_!@#$%^&*., ?)." )
    private String password;

    @NotNull(message = "Please enter your name")
    @Size(min = 4, max = 16,message = "Your name should be at least {min} chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your name must consist of the characters .")
    private String name;

    @NotNull(message = "Please enter your surname")
    @Size(min = 4, max = 16,message = "Your surname should be at least {min} chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your surname must consist of the characters .")
    private String surname;

    @NotNull(message = "Please enter your birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Your birthday can not be in the future")
    private LocalDate birthDay;

    @NotNull(message = "Please enter your birthplace")
    @Size(min = 2, max = 16,message = "Your birthplace should be at least {min} chars")
    @Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "Your birthplace must consist of the characters .")
    private String birthPlace;

    @NotNull(message = "Please enter your phone number")
    @Size(min = 12, max = 12,message = "Your phone number should be {min} characters long")
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    private String phoneNumber;

    @NotNull(message = "Please enter your gender")
    private Gender gender;
}
