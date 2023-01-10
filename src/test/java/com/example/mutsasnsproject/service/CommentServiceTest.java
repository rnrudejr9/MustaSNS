package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.fixture.CommentEntity;
import com.example.mutsasnsproject.fixture.PostEntity;
import com.example.mutsasnsproject.fixture.UserEntity;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.repository.AlarmRepository;
import com.example.mutsasnsproject.repository.CommentRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    CommentService commentService;

    CommentRepository commentRepository = mock(CommentRepository.class);
    AlarmRepository alarmRepository = mock(AlarmRepository.class);
    InValidChecker inValidChecker = mock(InValidChecker.class);

    User user;
    Post post;
    Comment comment;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository,alarmRepository,inValidChecker);
        user = UserEntity.get();
        post = PostEntity.get();
        comment = CommentEntity.get();
        comment.setCreatedAt(LocalDateTime.now());
    }

//    테스트 코드
//    ----------------------------------------------------------------------------
//    댓글 수정 실패 포스트 없음
//    댓글 수정 댓글이 없음
//    댓글 수정 작성자 다름
//    댓글 수정 수정완료
//    댓글 삭제 실패 포스트 없음
//    댓글 삭제 댓글이 없음
//    댓글 삭제 작성자 다름
//    댓글 삭제 삭제완료
//    ----------------------------------------------------------------------------

    @Test
    @DisplayName("댓글 수정 실패 : 포스트가 존재하지 않음")
    void updateFail1() {
        when(inValidChecker.postCheckById(post.getId())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentModify(user.getUserName(), post.getId(), new CommentRequest(),comment.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 실패 : 댓글이 존재하지 않음")
    void updateFail2() {
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(comment.getId())).thenThrow(new AppException(ErrorCode.COMMENT_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentModify(user.getUserName(), post.getId(), new CommentRequest(),comment.getId()));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 실패 : 작성자가 다름")
    void updateFail3() {
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(any())).thenReturn(comment);
        doThrow(new AppException(ErrorCode.USER_INCONSISTENCY,"")).when(inValidChecker).isInValidPermission(any(),any());
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentModify(user.getUserName(), post.getId(), new CommentRequest("cooment") ,comment.getId()));
        assertEquals(ErrorCode.USER_INCONSISTENCY, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 수정 완료")
    void success_test(){
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(any())).thenReturn(comment);
        assertDoesNotThrow(()->commentService.commentModify(user.getUserName(), post.getId(),new CommentRequest("commenttt"),comment.getId()));
        assertSame(commentService.commentModify(user.getUserName(), post.getId(),new CommentRequest("commenttt"),comment.getId()).getComment(),"commenttt");
        //결과물이 같으면 수정 성공!
    }


    @Test
    @DisplayName("댓글 삭제 실패 : 포스트가 존재하지 않음")
    void deleteFail1() {
        when(inValidChecker.postCheckById(post.getId())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentDelete(user.getUserName(), post.getId(),comment.getId()));
        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패 : 댓글이 존재하지 않음")
    void deleteFail2() {
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(comment.getId())).thenThrow(new AppException(ErrorCode.COMMENT_NOT_FOUND,""));
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentDelete(user.getUserName(), post.getId(),comment.getId()));
        assertEquals(ErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패 : 작성자가 다름")
    void deleteFail3() {
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(any())).thenReturn(comment);
        doThrow(new AppException(ErrorCode.USER_INCONSISTENCY,"")).when(inValidChecker).isInValidPermission(any(),any());
        AppException exception = Assertions.assertThrows(AppException.class,() -> commentService.commentDelete(user.getUserName(), post.getId() ,comment.getId()));
        assertEquals(ErrorCode.USER_INCONSISTENCY, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 완료")
    void success_test2(){
        when(inValidChecker.userCheck(user.getUserName())).thenReturn(user);
        when(inValidChecker.postCheckById(post.getId())).thenReturn(post);
        when(inValidChecker.commentCheckById(any())).thenReturn(comment);
        assertDoesNotThrow(()->commentService.commentDelete(user.getUserName(), post.getId(),comment.getId()));
        //결과물이 같으면 삭제 성공!
    }

}