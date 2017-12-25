package com.github.pig.admin.controller;

import com.github.pig.admin.dto.UserDto;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.json.JSON;
import com.xiaoleilu.hutool.json.JSONUtil;
import org.apache.commons.collections.MapUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author lengleng
 * @date 2017/12/25
 * UserController单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testUserGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin"));
    }

    @Test
    public void testUserAdd() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setRole(1);
        userDto.setNewpassword1("@1312312");
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.toJsonStr(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPostEdu() throws Exception {
        System.out.println(HttpUtil.post("http://eco.ahau.edu.cn/jc/dtrans", MapUtils.EMPTY_MAP));
    }
}