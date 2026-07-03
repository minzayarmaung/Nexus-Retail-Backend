package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name="users",indexes = {
        @Index(name = "index_user_email", columnList = "email"),
        @Index(name = "index_username", columnList = "username")
})
public class User extends Auditable {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private boolean isFirstTimeLogin;

    @Column(nullable = true)
    private boolean isExpired;

    @Column(nullable = true)
    private boolean cannotChangePassword;

    private boolean generatedPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns        = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;
}
