package com.example.mutsasnsproject.domain.entity;

import com.example.mutsasnsproject.domain.role.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime deletedAt;
    private String password;
    private UserRole role;
    private String userName;

    //한명의 User는 여러개의 Post 를 가질 수 있다.
    @OneToMany(mappedBy = "user")
    private List<Post> post = new ArrayList<>();

//    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }


}
