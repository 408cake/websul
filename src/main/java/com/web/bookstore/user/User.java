package com.web.bookstore.user;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_phone_number", columnNames = "phone_number")
})
public class User {

    public enum Gender { MALE, FEMALE, OTHER }
    public enum Role { USER, ADMIN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String name;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender = Gender.OTHER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = Role.USER;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    protected User() {}

    public User(String email, String passwordHash, String name, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role == null ? Role.USER : role;
        this.gender = Gender.OTHER;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public Gender getGender() { return gender; }
    public Role getRole() { return role; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
}
