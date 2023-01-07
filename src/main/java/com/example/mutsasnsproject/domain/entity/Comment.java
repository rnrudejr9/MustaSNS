package com.example.mutsasnsproject.domain.entity;

import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@Table(name = "comment")
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = now()  WHERE id=?")
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(Comment update){
        this.comment = update.comment;
        this.setLastModifiedAt(LocalDateTime.now());
    }

    public CommentResponse toResponse(){
        return CommentResponse.builder()
                .id(id)
                .comment(comment)
                .postId(post.getId())
                .userName(user.getUserName())
                .createdAt(getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .build();
    }

}
