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
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Client extends User {
}
