package com.example.mutsasnsproject.domain.entity;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = now()  WHERE id=?")
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    private String title;



    //여러 Post들중 하나씩은 userId 값을 가진다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//
    //하나의 post는 여러개의 coments를 가진다
    //cascadeType.ALL : 모든작업을 하위 엔티티까지 전파
    //orphanRemoval = true : 관계가 끊어진 자식개체들을 자동 제거
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Good> goods = new ArrayList<>();

    //userId 값 매핑

    public void update(Post update){
        this.body = update.body;
        this.title = update.title;
    }

    public PostDetailResponse toDetailResponse(){
        return PostDetailResponse.builder()
                .id(id)
                .title(title)
                .body(body)
                .lastModifiedAt(getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .createdAt(getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .userName(user.getUserName())
                .build();
    }

    public PostResponse toResponse(String message){
        return PostResponse.builder()
                .postId(id)
                .message(message)
                .build();
    }

}
