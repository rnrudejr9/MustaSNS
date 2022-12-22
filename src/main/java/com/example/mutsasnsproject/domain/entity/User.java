package com.example.mutsasnsproject.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime deletedAt;
    private String password;
    @CreatedDate
    private LocalDateTime registeredAt;
    //    private UserRole role;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String userName;

    //한명의 User는 여러개의 Post 를 가질 수 있다.
    @OneToMany(mappedBy = "user")
    private List<Post> post = new ArrayList<>();
}
