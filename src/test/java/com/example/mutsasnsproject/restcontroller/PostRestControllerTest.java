package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @Autowired
    ObjectMapper objectMapper;
    //자바 오브젝트를 json으로 바꿔줌

//    조회) 게시글 1개 조회
//    조회 실패) 로그인 하지 않음
//    조회) 전체 조회
//    작성) 게시글 1개 작성
//    작성 실패) 로그인 하지 않음
//    수정) 게시글 1개 수정
//    수정 실패) 로그인 하지 않음
//    수정 실패) 게시글 없음
//    수정 실패) 작성자와 사용자가 다름
//    수정 실패) 데이터베이스 오류
//    삭제) 게시글 1개 삭제
//    삭제 실패) 로그인 하지 않음
//    삭제 실패) 게시글 없음
//    삭제 실패) 작성자와 사용자가 다름
//    삭제 실패) 데이터베이스 오류
//    댓글) 댓글 1개 작성
//    댓글 실패) 로그인 하지 않음
//    좋아요) 좋아요 성공
//    좋아요 실패) 좋아요 실패 로그인 하지 않음
//    좋아요 실패) 게시글이 존재하지 않을 경우
//    마이피드) 마이피드 조회성공
//    마이피드 실패) 로그인 하지 않음
//    ---------------------------------------------
//    알림) 알림 성공
//    댓글 조회)
//    댓글 삭제)
//

    @DisplayName("조회) 게시글 1개 조회 성공")
    @Test
    @WithMockUser
    public void Test1() throws Exception {
        when(postService.get(any(),any())).thenReturn(new PostDetailResponse(1L,"","","","",""));

        mockMvc.perform(get("/api/v1/posts/"+"1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(new PostDetailResponse(1L,"","","","",""))))
                    .andDo(print())
                    .andExpect(status().isOk());
    }
    @DisplayName("조회) 게시글 1개 조회 실패 : 로그인 하지 않음")
    @Test
    @WithMockUser
    public void view_fail() throws Exception {
        when(postService.list(any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(get("/api/v1/posts/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostDetailResponse(1L,"","","","",""))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("작성) 게시글 1개 작성 성공")
    @Test
    @WithMockUser
    public void Test2() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();
        when(postService.add("userName",postRequest.getBody(),postRequest.getTitle())).thenReturn(new PostResponse("message",1L));

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @DisplayName("작성) 게시글 1개 작성 실패 : 권한 없음")
    @Test
    @WithMockUser
    public void Test3() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();
        when(postService.add(any(),any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("수정) 게시글 1개 수정 성공")
    @Test
    @WithMockUser
    public void Test4() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();

        when(postService.modify(any(),any(),any())).thenReturn(new PostResponse("Message",1L));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser // 인증 되지 않은 상태
    @DisplayName("수정) 게시글 1개 수정 실패 : 권한 없음")
    void modify_fail1() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();

        when(postService.modify(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("수정) 게시글 1개 수정 실패 : 선택 게시글 불일치")
    void modify_fail2() throws Exception {

        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();

        when(postService.modify(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,"포스트가 없음"));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("수정) 게시글 1개 수정 실패 : 작성자 불일치")
    void modify_fail3() throws Exception {


        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();
        when(postService.modify(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,"작성자 불일치"));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("수정) 게시글 1개 수정 실패 : 데이터베이스 에러")
    void modify_fail4() throws Exception {

        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();

        when(postService.modify(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,"데이터베이스 에러"));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }


    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("삭제) 게시글 1개 삭제 성공")
    void delete_success() throws Exception {
        when(postService.delete(any(),any())).thenReturn(new PostResponse("message",1L));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser // 인증 된지 않은 상태
    @DisplayName("삭제) 게시글 1개 삭제 실패 : 권한없음")
    void delete_fail1() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("삭제) 게시글 1개 삭제 실패 : 내용 불일치")
    void delete_fail2() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("삭제) 게시글 1개 삭제 실패 : 작성자 불일치")
    void delete_fail3() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }


    @Test
    @WithMockUser   // 인증된 상태
    @DisplayName("삭제) 게시글 1개 삭제 실패 : 데이터베이스 에러")
    void delete_fail4() throws Exception {

        when(postService.delete(any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR,""));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 1개 작성 성공")
    void comment_test() throws Exception {
        String userName = "userName";

        when(postService.commentAdd(any(),any(),any())).thenReturn(CommentResponse.builder().comment("hello").build());

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
    @WithMockUser
    @DisplayName("댓글 1개 작성 실패 : 권한 없음")
    void comment_test1() throws Exception {
        String userName = "userName";
        when(postService.commentAdd(any(),any(),any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));
        CommentRequest commentRequest = CommentRequest.builder().comment("hello").build();
        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest))
                )
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @DisplayName("댓글) 전체 조회 성공")
    @Test
    @WithMockUser
    public void comment_find() throws Exception {
        when(postService.commentList(any(),any())).thenReturn(new CommentListResponse());

        mockMvc.perform(get("/api/v1/posts/1" + "/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("댓글) 삭제 성공")
    @Test
    @WithMockUser
    public void comment_delete() throws Exception {
        when(postService.delete(any(),any())).thenReturn(new PostResponse());

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("댓글) 삭제 실패 : 사용자와 작성자가 다름")
    @Test
    @WithMockUser
    public void comment_delete2() throws Exception {
        when(postService.delete(any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("좋아요 실패 : Login하지 않은 경우")
    @WithAnonymousUser // Login하지 않은 경우를 표현
    void good_fail1() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("좋아요 실패 : 게시물이 존재하지 않는 경우")
    @WithMockUser
    void like_fail2() throws Exception {
        doThrow(new AppException(ErrorCode.POST_NOT_FOUND,""))
                .when(postService).postGood(any(), any());

        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("좋아요 성공 ")
    @WithMockUser
    void like_success() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        ;
    }

    @Test
    @DisplayName("마이피드 조회 성공")
    @WithMockUser
    void myPage() throws Exception {
        when(postService.myPages(any(),any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("마이피드 조회 실패 ) 로그인 하지 않음")
    @WithMockUser
    //토큰을 발급했다 - 인증정보가 없는 토큰이 일치하지않는
    void myPage_fail() throws Exception {
        when(postService.myPages(any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));
        mockMvc.perform(get("/api/v1/posts/my")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;
    }



}