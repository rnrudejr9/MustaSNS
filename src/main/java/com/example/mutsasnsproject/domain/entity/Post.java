package com.example.mutsasnsproject.domain.entity;

import lombok.*;
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
@Getter
@NoArgsConstructor
@Setter
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    //여러 Post들중 하나씩은 userId 값을 가진다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//
    //하나의 post는 여러개의 coments를 가진다
    @OneToMany(mappedBy = "post")
    private List<Comment> likes = new ArrayList<>();

    //userId 값 매핑

    public void update(Post update){
        this.body = update.body;
        this.title = update.title;
    }
}
