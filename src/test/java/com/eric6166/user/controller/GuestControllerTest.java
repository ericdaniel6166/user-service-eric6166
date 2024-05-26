package com.eric6166.user.controller;

import com.eric6166.base.utils.TestConst;
import com.eric6166.user.service.TestService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@WebMvcTest(controllers = {GuestController.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
class GuestControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    TestService testService;

    @Test
    void test() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/guest" + "/test")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .authorities(new SimpleGrantedAuthority(TestConst.ROLE_CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("guest test"));
    }
}