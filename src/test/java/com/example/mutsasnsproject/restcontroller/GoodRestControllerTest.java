package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.infra.NotificationInterceptor;
import com.example.mutsasnsproject.service.GoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoodRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class GoodRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    GoodService goodSerivce;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    NotificationInterceptor notificationInterceptor;


//    ----------------------------------------------------------------------------
//    좋아요 누르기 성공
//    좋아요 누르기 실패(1) - 로그인 하지 않은 경우
//    좋아요 누르기 실패(2) - 해당 Post가 없는 경우
//    ----------------------------------------------------------------------------

    @Test
    @DisplayName("좋아요 성공 ")
    @WithMockUser
    void like_success() throws Exception {
        when(goodSerivce.postGood(any(),any())).thenReturn("message");
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(1L)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
        ;
    }

    @Test
    @DisplayName("좋아요 실패 : Login하지 않은 경우")
    @WithAnonymousUser
        // Login하지 않은 경우를 표현
    void good_fail1() throws Exception {
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("좋아요 실패 : 게시글이 없을경우")
    @WithMockUser
        // Login하지 않은 경우를 표현
    void good_fail2() throws Exception {

        when(goodSerivce.postGood(any(),any())).thenThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));
        mockMvc.perform(post("/api/v1/posts/1/likes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}