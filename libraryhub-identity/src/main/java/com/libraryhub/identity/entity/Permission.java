package com.libraryhub.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(
        name = "PERMISSION",
        uniqueConstraints = @UniqueConstraint(columnNames = "NAME")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERMISSION_ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;
    // BORROW_BOOK, ADD_BOOK, MANAGE_USERS, etc.

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "STATUS", nullable = false)
    private String status; // ACTIVE, INACTIVE

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;


}





//    @ManyToMany(mappedBy = "permissions")
//    private Set<Role> roles = new HashSet<>();
