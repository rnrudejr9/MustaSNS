package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.service.PostService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoodRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class GoodRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @Autowired
    ObjectMapper objectMapper;

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


}