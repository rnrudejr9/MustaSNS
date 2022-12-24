package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.fixture.PostEntityFixture;
import com.example.mutsasnsproject.fixture.TestInfoFixture;
import com.example.mutsasnsproject.fixture.UserEntityFixture;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    @DisplayName("등록 성공")
    void post_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);

        Assertions.assertDoesNotThrow(() -> postService.createPost(fixture.getUserName(), fixture.getBody(), fixture.getTitle()));
    }

    @Test
    @DisplayName("조회 성공")
    void success_post_get() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = new User();
        user.setUserName(fixture.getUserName());

        Post post = PostEntityFixture.get(fixture.getUserName(), fixture.getPassword());

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));

        PostDetailResponse postDetailResponse = postService.detail(fixture.getPostId());
        assertEquals(fixture.getUserName(), postDetailResponse.getUserName());
    }

    @Test
    @DisplayName("삭제 성공")
    @WithMockUser
    void post_delete_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(postRepository.findById(fixture.getPostId()))
                .thenReturn(Optional.of(mockPostEntity));
        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));

        when(mockPostEntity.getUser()).thenReturn(UserEntityFixture.get(fixture.getUserName(), fixture.getPassword()));
        PostResponse postResponse = postService.delete(fixture.getUserName(), fixture.getPostId());
        System.out.println(postResponse);
    }


}