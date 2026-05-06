package com.nexusretail.data.models;

import com.nexusretail.data.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "employees", indexes = {
    @Index(name = "index_employee_shop", columnList = "shopId"),
    @Index(name = "index_employee_user", columnList = "userId")
})
public class Employee extends Auditable {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNo;

    @Column(nullable = true)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private String address;

    @Column(nullable = false)
    private Long shopId;

    @Column(nullable = true)
    private String position; // HR, SALESPERSON, etc.

    @Column(nullable = true)
    private LocalDate hireDate;

    @Column(nullable = true)
    private String salary; // Could be BigDecimal, but keeping as String for flexibility

    @Column(nullable = true)
    private String nrc; // Myanmar NRC format: "12/LaKaNa(N)318903"

    @Column(nullable = true)
    private String profilePictureUrl; // URL to employee profile picture

    // Optional relationship to User (not all employees have user accounts)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
