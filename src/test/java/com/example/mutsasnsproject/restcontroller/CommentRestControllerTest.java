package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CommentService commentService;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    NotificationInterceptor notificationInterceptor;

//    테스트 코드
//    ----------------------------------------------------------------------------
//    댓글 작성 성공
//    댓글 작성 실패 : 로그인 하지 않음
//    댓글 작성 실패 : 게시글 존재하지 않음
//    댓글 수정 성공
//    댓글 수정 실패 : 로그인 하지 않음
//    댓글 수정 실패 : 게시글 존재하지 않음
//    댓글 수정 실패 : 작성자 불일치
//    댓글 수정 실패 : 데이터베이스 에러
//    댓글 삭제 성공
//    댓글 삭제 실패 : 로그인 하지 않음
//    댓글 삭제 실패 : 게시글 존재하지 않음
//    댓글 삭제 실패 : 작성자 불일치
//    댓글 삭제 실패 : 데이터베이스 에러
//    댓글 조회 성공
//    ----------------------------------------------------------------------------



    @Test
    @WithMockUser
    @DisplayName("댓글 1개 작성 성공")
    void comment_test() throws Exception {
        String userName = "userName";

        when(commentService.commentAdd(any(),any(),any())).thenReturn(CommentResponse.builder().comment("hello").build());

        CommentRequest commentRequest = CommentRequest.builder().comment("hello").build();
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("댓글 작성 실패 : 로그인 하지 않음")
    void comment_test1() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder().comment("hello").build();
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 작성 실패 : 게시글 존재하지 않음")
    void comment_test2() throws Exception {
        CommentRequest commentRequest = CommentRequest.builder().comment("hello").build();
        when(commentService.commentAdd(any(),any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }


    @DisplayName("댓글 수정 성공")
    @Test
    @WithMockUser
    public void comment_modify() throws Exception {

        when(commentService.commentModify(any(),any(),any(),any())).thenReturn(new CommentResponse(1L,"","",1L,"",""));
//        given(commentService.modify(any(),any(),any())).willReturn(postResponse);

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest())))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.postId").value(1))
                .andExpect(jsonPath("$.result.userName").value(""))
                .andExpect(jsonPath("$.result.comment").value(""))
                .andExpect(jsonPath("$.result.createdAt").value(""))
                .andExpect(jsonPath("$.result.lastModifiedAt").value(""))
                .andExpect(status().isOk());
    }

    @DisplayName("댓글 수정 실패 : 로그인 하지 않음")
    @Test
    @WithMockUser
    public void comment_modify_fail() throws Exception {
        when(commentService.commentModify(any(),any(),any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest())))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글 수정 실패 : 게시글이 존재하지 않음")
    @Test
    @WithMockUser
    public void comment_modify_fail2() throws Exception {
        when(commentService.commentModify(any(),any(),any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest())))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isNotFound());
    }

    @DisplayName("댓글 수정 실패 : 작성자 불일치")
    @Test
    @WithMockUser
    public void comment_modify_fail3() throws Exception {
        when(commentService.commentModify(any(),any(),any(),any())).thenThrow(new AppException(ErrorCode.USER_INCONSISTENCY,""));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest())))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글 수정 실패 : 데이터베이스 에러")
    @Test
    @WithMockUser
    public void comment_modify_fail4() throws Exception {
        when(commentService.commentModify(any(),any(),any(),any())).thenThrow(new AppException(ErrorCode.DATABASE_ERROR,""));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostRequest())))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("댓글) 삭제 성공")
    @Test
    @WithMockUser
    public void comment_delete() throws Exception {
        when(commentService.commentDelete(any(),any(),any())).thenReturn(new CommentResponse());

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(status().isOk());
    }

    @DisplayName("댓글) 삭제 실패 : 로그인하지않음")
    @Test
    @WithMockUser
    public void comment_delete_fail() throws Exception {
        when(commentService.commentDelete(any(),any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글) 삭제 실패 : 게시글이 없음")
    @Test
    @WithMockUser
    public void comment_delete_fail2() throws Exception {
        when(commentService.commentDelete(any(),any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("댓글) 삭제 실패 : 작성자가 다름")
    @Test
    @WithMockUser
    public void comment_delete_fail3() throws Exception {
        when(commentService.commentDelete(any(),any(),any())).thenThrow(new AppException(ErrorCode.USER_INCONSISTENCY,""));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("댓글) 삭제 실패 : 데이터베이스에러")
    @Test
    @WithMockUser
    public void comment_delete_fail4() throws Exception {
        when(commentService.commentDelete(any(),any(),any())).thenThrow(new AppException(ErrorCode.DATABASE_ERROR,""));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("댓글) 전체 조회 성공")
    @Test
    @WithMockUser
    public void comment_find() throws Exception {
        when(commentService.commentList(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/1" + "/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
