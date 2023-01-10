package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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

    @MockBean
    NotificationInterceptor notificationInterceptor;
//    테스트 코드
//    ----------------------------------------------------------------------------
//    조회 성공) 게시글 1개 조회
//    조회 실패) 로그인 하지 않음
//    조회 성공) 전체 조회
//    작성 성공) 게시글 1개 작성
//    작성 실패) jwt 토큰값 없음
//    작성 실패) 올바르지않는 토큰값
//    수정 성공) 게시글 1개 수정
//    수정 실패) 로그인 하지 않음
//    수정 실패) 게시글 없음
//    수정 실패) 작성자와 사용자가 다름
//    수정 실패) 데이터베이스 오류
//    삭제 성공) 게시글 1개 삭제
//    삭제 실패) 로그인 하지 않음
//    삭제 실패) 게시글 없음
//    삭제 실패) 작성자와 사용자가 다름
//    삭제 실패) 데이터베이스 오류
//    마이피드 조회 성공
//    마이피드 조회 실패 : 로그인 안함
//    ----------------------------------------------------------------------------
//    <테스트 코드 작성 팁>
//    0. 테스크 등록하기 : 내가 뭘 하고 있지?
//    1. 요구사항 확인
//    2. Test를 먼저 만들고
//    3. Service는 null을 리턴하게 처리

    @BeforeEach
    public void setUp(){

    }

    @DisplayName("조회) 게시글 1개 조회 성공")
    @Test
    @WithMockUser
    public void Test1() throws Exception {
        when(postService.get(any(),any())).thenReturn(new PostDetailResponse(1L,"","","","","",1,1));

        mockMvc.perform(get("/api/v1/posts/"+"1")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.title").value(""))
                .andExpect(jsonPath("$.result.body").value(""))
                .andExpect(jsonPath("$.result.userName").value(""))
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
                        .content(objectMapper.writeValueAsBytes(new PostDetailResponse(1L,"","","","","",1,1))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("조회) 게시글 전체 조회 성공")
    @Test
    @WithMockUser
    public void view_all() throws Exception {
        when(postService.list(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/"+"1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(status().isOk());
    }

    @DisplayName("작성) 게시글 1개 작성 성공")
    @Test
    @WithMockUser
    public void write() throws Exception {
        when(postService.add(any(),any())).thenReturn(new PostResponse("message",1L));

        mockMvc.perform(post("/api/v1/posts")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostRequest("body","title"))))
                .andDo(print())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.message").value("message"))
                .andExpect(status().isOk());
    }
    @DisplayName("작성) 게시글 1개 작성 실패 : jwt 토큰이 없을 경우")
    @Test
    @WithMockUser
    public void writeFail() throws Exception {
        PostRequest postRequest = PostRequest.builder().title("title").body("body").build();
        when(postService.add(any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("글 작성 실패 Test 2 : JWT Token이 유효하지 않은 경우")
    @WithMockUser
    public void writeFail2() throws Exception {

        when(postService.add(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(post("/api/v1/posts")
                        .content(objectMapper.writeValueAsString(new PostRequest("body","title")))
                        .header("Authroization", "Bearer JwtToken123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
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
    @DisplayName("수정) 게시글 1개 수정 실패 : 게시글이 없음")
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
