package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.PostEntityFixture;
import com.example.mutsasnsproject.fixture.TestInfoFixture;
import com.example.mutsasnsproject.fixture.UserEntityFixture;
import com.example.mutsasnsproject.repository.CommentRepository;
import com.example.mutsasnsproject.repository.GoodRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PostServiceTest {

    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);

    UserRepository userRepository = mock(UserRepository.class);

    CommentRepository commentRepository = mock(CommentRepository.class);

    GoodRepository goodRepository = mock(GoodRepository.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository,commentRepository,goodRepository);
    }

//    테스트 코드
//    ----------------------------------------------------------------------------
//    게시글 등록 성공)
//    게시글 등록 실패) 유저존재하지않음
//    게시글 조회 성공)
//    게시글 삭제 성공)
//    게시글 삭제 실패) 유저 존재하지 않음
//    게시글 삭제 실패) 삭제할 게시글이 없음
//    게시글 삭제 실패) 삭제할 게시글의 작성자와 사용자가 다름
//    게시글 수정 성공)
//    게시글 수정 실패) 유저 존재하지 않음
//    게시글 수정 실패) 수정할 게시글이 없음
//    게시글 수정 실패) 수정할 게시글의 작성자와 사용자가 다름
//    -----------------------------------------------------------------------------


    @Test
    @DisplayName("조회) 성공")
    void success_post_get() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User userEntity = mock(User.class);
        Post post = PostEntityFixture.get(fixture.getUserName(), "1q2w3e4r");
        post.setCreatedAt(LocalDateTime.now());
        post.setLastModifiedAt(LocalDateTime.now());

        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(post));
//        when(userRepository.save(any())).thenReturn(userEntity);

        PostDetailResponse postDetailResponse = postService.get(fixture.getUserName(),fixture.getPostId());
        assertEquals(fixture.getUserName(), postDetailResponse.getUserName());
    }

    @Test
    @DisplayName("등록) 성공")
    void post_success() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        Post mockPostEntity = mock(Post.class);
        User mockUserEntity = mock(User.class);

        when(userRepository.findByUserName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUserEntity));
        when(postRepository.save(any()))
                .thenReturn(mockPostEntity);


        Assertions.assertDoesNotThrow(() -> postService.add(fixture.getUserName(), fixture.getBody(), fixture.getTitle()));
    }

    @Test
    @DisplayName("등록) 실패 : 유저 존재하지 않음")
    void post_fail_no_user() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.add(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("수정) 성공")
    void modify_success(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserName(),fixture.getPassword())));
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(PostEntityFixture.get(fixture.getUserName(), fixture.getPassword())));

        PostRequest postRequest = PostRequest.builder().body("modify").title("modify").build();
        Assertions.assertDoesNotThrow(() -> postService.modify(fixture.getUserName(), fixture.getPostId(),postRequest));
    }

    @Test
    @DisplayName("수정) 실패 : 유저 존재하지 않을 경우")
    void modify_fail_1(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        PostRequest postRequest = PostRequest.builder().body("modify").title("modify").build();
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.modify(fixture.getUserName(), fixture.getPostId(), postRequest));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND,exception.getErrorCode());

    }

    @Test
    @DisplayName("수정) 실패 : 수정할 포스트가 없을 경우")
    void modify_fail_2(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        PostRequest postRequest = PostRequest.builder().body("modify").title("modify").build();
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(user));
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.modify(fixture.getUserName(), fixture.getPostId(), postRequest));

        assertEquals(ErrorCode.POST_NOT_FOUND,exception.getErrorCode());
    }

    @Test
    @DisplayName("수정) 실패 : 사용자와 수정할 포스트의 작성자가 다를 경우")
    void modify_fail_3(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = UserEntityFixture.get("user1","password");
        User user2 = UserEntityFixture.get("user2","password");
        PostRequest postRequest = PostRequest.builder().body("modify").title("modify").build();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(user2.getUserName(),user2.getPassword())));
        //findbyusername 하면 리턴값으로 user2에 대한 user 정보를 제공
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(PostEntityFixture.get(user.getUserName(),user.getPassword())));
        //findbyid 하면 리턴값으로 user 의 대한 post 정보 제공
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.modify(fixture.getUserName(), 1L, postRequest));
        //post의 작성자와 user의 id값과 다르기 때문에 에러 코드 맞음
        assertEquals(ErrorCode.INVALID_PERMISSION,exception.getErrorCode());
    }

    @Test
    @DisplayName("삭제) 성공")
    void delete(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userRepository.findByUserName(any())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserName(),fixture.getPassword())));
        when(postRepository.findById(any())).thenReturn(Optional.of(PostEntityFixture.get(fixture.getUserName(), fixture.getPassword())));
        Assertions.assertDoesNotThrow(() -> postService.delete(fixture.getUserName(),fixture.getPostId()));
    }
    @Test
    @DisplayName("삭제) 실패 : 유저 존재하지 않음")
    void delete_fail3() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(fixture.getUserName(), fixture.getPostId()));

        assertEquals(ErrorCode.USERNAME_NOT_FOUND,exception.getErrorCode());
    }
    @Test
    @DisplayName("삭제) 실패 : 삭제할 포스트가 없을 경우")
    void delete_fail2(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserName(),fixture.getPassword())));
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(fixture.getUserName(), fixture.getPostId()));

        assertEquals(ErrorCode.POST_NOT_FOUND,exception.getErrorCode());
    }
    @Test
    @DisplayName("삭제) 실패 : 사용자와 삭제할 포스트의 작성자가 다를 경우")
    void delete_fail_3(){
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        User user = UserEntityFixture.get("user1","password");
        User user2 = UserEntityFixture.get("user2","password");
        PostRequest postRequest = PostRequest.builder().body("modify").title("modify").build();
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(user2.getUserName(),user2.getPassword())));
        //findbyusername 하면 리턴값으로 user2에 대한 user 정보를 제공
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(PostEntityFixture.get(user.getUserName(),user.getPassword())));
        //findbyid 하면 리턴값으로 user 의 대한 post 정보 제공
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(fixture.getUserName(), 1L));
        //post의 작성자와 user의 id값과 다르기 때문에 에러 코드 맞음
        assertEquals(ErrorCode.INVALID_PERMISSION,exception.getErrorCode());
    }


}