package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.PostEntity;
import com.example.mutsasnsproject.fixture.UserEntity;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.repository.*;
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
import static org.mockito.Mockito.*;


public class PostServiceTest {

    PostService postService;

    PostRepository postRepository = mock(PostRepository.class);

    UserRepository userRepository = mock(UserRepository.class);

    InValidChecker inValidChecker = mock(InValidChecker.class);

    NotificationInterceptor notificationInterceptor = mock(NotificationInterceptor.class);

    User user;
    Post post;

    @BeforeEach
    void setUp() {

        postService = new PostService(postRepository, inValidChecker);
        user = UserEntity.get();
        post = PostEntity.get();

    }

//    테스트 코드
//    ----------------------------------------------------------------------------
//    게시글 조회 성공)
//    게시글 등록 성공)
//    게시글 등록 실패) 유저존재하지않음
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
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        PostDetailResponse postDetailResponse = postService.get(user.getUserName(),post.getId());
        assertEquals(user.getUserName(), postDetailResponse.getUserName());
    }

    @Test
    @DisplayName("등록) 성공")
    void post_success() {
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(postRepository.save(any())).thenReturn(post);
        Assertions.assertDoesNotThrow(() -> postService.add(user.getUserName(), new PostRequest()));
    }

    @Test
    @DisplayName("등록) 실패 : 유저 존재하지 않음")
    void post_fail_no_user() {
        when(inValidChecker.userCheck(any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));
        when(postRepository.save(any())).thenReturn(new Post());
        AppException exception = Assertions.assertThrows(AppException.class, () -> postService.add(user.getUserName(),new PostRequest()));
        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("삭제) 성공")
    void delete(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenReturn(post);
        Assertions.assertDoesNotThrow(() -> postService.delete(user.getUserName(),post.getId()));
    }
    @Test
    @DisplayName("삭제) 실패 : 유저 존재하지 않음")
    void delete_fail3() {
        when(inValidChecker.userCheck(any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND,exception.getErrorCode());
    }
    @Test
    @DisplayName("삭제) 실패 : 삭제할 포스트가 없을 경우")
    void delete_fail2(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(user.getUserName(), post.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND,exception.getErrorCode());
    }
    @Test
    @DisplayName("삭제) 실패 : 사용자와 삭제할 포스트의 작성자가 다를 경우")
    void delete_fail_3(){

        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenReturn(post);
        doThrow(new AppException(ErrorCode.INVALID_PERMISSION,"")).when(inValidChecker).isInvalidPermission(any(),any());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(user.getUserName(), post.getId()));
        //post의 작성자와 user의 id값과 다르기 때문에 에러 코드 맞음
        assertEquals(ErrorCode.INVALID_PERMISSION,exception.getErrorCode());
    }

    @Test
    @DisplayName("수정) 성공")
    void modify_success(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenReturn(post);
        Assertions.assertDoesNotThrow(() -> postService.modify(user.getUserName(),post.getId(),new PostRequest()));
    }

    @Test
    @DisplayName("수정) 실패 : 유저 존재하지 않을 경우")
    void modify_fail_1(){
        Post post = mock(Post.class);
        when(inValidChecker.userCheck(any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.modify(user.getUserName(), post.getId(),new PostRequest()));
        assertEquals(ErrorCode.USERNAME_NOT_FOUND,exception.getErrorCode());
    }

    @Test
    @DisplayName("수정) 실패 : 수정할 포스트가 없을 경우")
    void modify_fail_2(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.modify(user.getUserName(), post.getId(),new PostRequest()));
        assertEquals(ErrorCode.POST_NOT_FOUND,exception.getErrorCode());
    }

    @Test
    @DisplayName("수정) 실패 : 사용자와 수정할 포스트의 작성자가 다를 경우")
    void modify_fail_3(){
        when(inValidChecker.userCheck(any())).thenReturn(user);
        when(inValidChecker.postCheckById(any())).thenReturn(post);
        doThrow(new AppException(ErrorCode.INVALID_PERMISSION,"")).when(inValidChecker).isInvalidPermission(any(),any());
        AppException exception = Assertions.assertThrows(AppException.class,() -> postService.delete(user.getUserName(), post.getId()));
        //post의 작성자와 user의 id값과 다르기 때문에 에러 코드 맞음
        assertEquals(ErrorCode.INVALID_PERMISSION,exception.getErrorCode());
    }




}