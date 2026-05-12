package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name="users",indexes = {
        @Index(name = "index_user_email", columnList = "email")
})
public class User extends Auditable {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Long shopId;

    @Column(nullable = false)
    private String phoneNo;

     @ManyToOne
     @JoinColumn(name = "role_id", referencedColumnName = "id")
     private Role role;

}
