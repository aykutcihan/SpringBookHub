package com.project.springbookhub.model.concretes;

import com.project.springbookhub.model.enums.RoleType;
import lombok.*;

import javax.persistence.*;

/**
 * Role entity represents the role of a user in the system,
 * such as ROLE_USER or ROLE_ADMIN.
 */

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType roleType;


}
