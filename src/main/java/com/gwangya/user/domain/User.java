package com.gwangya.user.domain;

import com.gwangya.global.domain.BaseEntity;
import com.gwangya.user.domain.vo.Email;
import com.gwangya.user.domain.vo.Password;
import com.gwangya.user.shared.dto.UserCreateCommand;
import com.gwangya.user.repository.UserRepository;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    protected User() {
    }

    public User(Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.email = email;
        this.password = password;
    }

    public static User of(final UserCreateCommand userCreateCommand, final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        return new User(
                Email.of(userCreateCommand.getEmail(), userRepository),
                Password.of(userCreateCommand.getPassword(), passwordEncoder),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
}
