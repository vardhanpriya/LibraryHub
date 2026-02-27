package com.libraryhub.identity.entity;


import com.libraryhub.identity.enums.RegistrationType;
import com.libraryhub.identity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.libraryhub.identity.enums.AuthProviderType;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "USERS",
        indexes = {
                @Index(name = "IDX_USER_EMAIL", columnList = "EMAIL"),
                @Index(name = "IDX_USER_MEMBERSHIP", columnList = "MEMBERSHIP_NUMBER")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "NAME", length = 150)
    private String name;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "REGISTRATION_TYPE")
    private RegistrationType registrationType;

    @Column(name = "MEMBERSHIP_NUMBER", unique = true, length = 50)
    private String membershipNumber;

    @Column(name = "ID_TYPE", length = 50)
    private String idType; // for library staff only aadhar / voterCard

    @Column(name = "ID_NUMBER", length = 100)
    private String idNumber; // for library staff only  idNumber

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS", nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTH_PROVIDER_TYPE", nullable = false)
    private AuthProviderType authProviderType;

    @Column(name = "PROVIDER_ID", unique = true)
    private String providerId;

    // ======================
    // USER ↔ ROLE
    // ======================
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}

