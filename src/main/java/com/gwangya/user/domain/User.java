package com.gwangya.user.domain;

import com.gwangya.global.domain.BaseEntity;
import com.gwangya.user.domain.vo.Email;
import jakarta.persistence.*;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;


}
