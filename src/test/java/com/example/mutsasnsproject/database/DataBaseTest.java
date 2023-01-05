package com.example.mutsasnsproject.database;

import com.example.mutsasnsproject.domain.entity.Good;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.repository.GoodRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class DataBaseTest {
    @Autowired
    GoodRepository goodRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("soft delete 테스트")
    void postSaveLikeDeletePost() {
        Post postEntity = Post.builder()
                .title("eee")
                .body("fff")
                .build();
        // 글쓰기
        Post savedPost = postRepository.save(postEntity);

        Good goodEntity = Good.builder()
                .post(savedPost)
                .build();
        // 좋아요 누르기
        Good savedGood = goodRepository.save(goodEntity);
        System.out.println(savedGood.getPost().getId());
        System.out.println(savedGood.getId());

        // good를 누르고 개수 세기
        System.out.printf("good누르고 개수 세기:%d\n", goodRepository.count());

        // 좋아요 지우기
        goodRepository.deleteAllByPost(savedPost);
        System.out.printf("지우고 개수 세기:%d\n", goodRepository.count());
        Optional<Good> goodEntity1 = goodRepository.findById(savedGood.getId());
        System.out.println(goodEntity1.isEmpty());
        System.out.printf("deleted_at:%s\n", goodEntity1.get().getDeletedAt());

        // 글 지우기
        postRepository.delete(savedPost);

    }
}