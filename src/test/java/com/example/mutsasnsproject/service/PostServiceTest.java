package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.fixture.PostEntityFixture;
import com.example.mutsasnsproject.fixture.TestInfoFixture;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostServiceTest {
    PostService postService;
    PostRepository postRepository= Mockito.mock(PostRepository.class);
    UserRepository userRepository= Mockito.mock(UserRepository.class);
    @BeforeEach
    void setUp(){
        postService=new PostService(postRepository,userRepository);
    }

    @Test
    @DisplayName("조회 성공")
    void success_post_get() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User userEntity = new User();
        userEntity.setUserName(fixture.getUserName());

        Post postEntity = PostEntityFixture.get(fixture.getUserName(), "1q2w3e4r");

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(postEntity));

        when(postRepository.save(any()))
                .thenReturn(postEntity);

        PostDetailResponse response = postService.detail(fixture.getPostId());
        assertEquals(fixture.getUserName(), response.getUserName());
    }


    //레포지토리는 모키토를 이용해 만들어줌 -> db 의존성을 빼기 위함!
}