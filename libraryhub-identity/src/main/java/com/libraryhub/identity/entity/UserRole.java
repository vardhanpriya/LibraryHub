//package com.libraryhub.identity.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "USER_ROLE")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserRole {
//
//    @EmbeddedId
//    private UserRoleId id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("userId")
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("roleId")
//    @JoinColumn(name = "ROLE_ID", nullable = false)
//    private Role role;
//
//    @Column(name = "ASSIGNED_AT", nullable = false)
//    private LocalDateTime assignedAt;
//}
//
//UserRole userRole = UserRole.builder()
//        .id(new UserRoleId(user.getId(), role.getRoleId()))
//        .user(user)
//        .role(role)
//        .assignedAt(LocalDateTime.now())
//        .build();
//
//userRoleRepository.save(userRole);


