package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostListResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.dto.user.UserLoginRequest;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.service.PostService;
import com.example.mutsasnsproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    @WithAnonymousUser
    public void Test3() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();
        when(postService.add("userName",postRequest.getBody(),postRequest.getTitle())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

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
    @WithAnonymousUser // 인증 되지 않은 상태
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
    @WithAnonymousUser // 인증 된지 않은 상태
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






}