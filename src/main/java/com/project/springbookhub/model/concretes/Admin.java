package com.project.springbookhub.model.concretes;

import com.project.springbookhub.model.abstracts.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Admin extends User {

    private boolean built_in;

}
