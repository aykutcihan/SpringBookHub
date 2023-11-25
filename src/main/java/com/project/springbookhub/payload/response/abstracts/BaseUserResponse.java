package com.project.springbookhub.payload.response.abstracts;

import com.project.springbookhub.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUserResponse {

    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDay;
    private String birthPlace;
    private String phoneNumber;
    private Gender gender;
}
