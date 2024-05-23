package com.eric6166.user.controller;

import com.eric6166.base.utils.TestConst;
import com.eric6166.user.service.TestService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

@WebMvcTest(controllers = {TestController.class})
@FieldDefaults(level = AccessLevel.PRIVATE)
class TestControllerTest {

    private static final String URL_TEMPLATE = "/test";

    @Autowired
    MockMvc mvc;

    @MockBean
    TestService testService;

    @Test
    void testFeign() throws Exception {
        var service = "inventory";
        var method = TestConst.PRODUCT_TEST;
        var expected = "product test";

        Mockito.when(testService.testFeign(service, method, null)).thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders
                        .get(URL_TEMPLATE + "/feign")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .authorities(new SimpleGrantedAuthority(TestConst.ROLE_CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("service", service)
                        .param("method", method))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expected));

    }

    @Test
    void testAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(URL_TEMPLATE + "/admin")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .authorities(new SimpleGrantedAuthority(TestConst.ROLE_ADMIN)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("test admin"));
    }

    @Test
    void testAdmin_thenReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(URL_TEMPLATE + "/admin")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .authorities(new SimpleGrantedAuthority(TestConst.ROLE_ADMIN)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("test admin"));
    }


}