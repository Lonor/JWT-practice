package com.springboot.lawrence;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LawrenceApplicationTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testHello() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 4000);
        jsonObject.put("msg", "您的token不合法或者过期了，请重新登陆");
        ResultActions perform = mvc.perform(MockMvcRequestBuilders.get("/api/test"));
        MvcResult mvcResult = perform.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString, is(jsonObject.toJSONString()));
    }

    @Test
    public void login() throws Exception {
        JSONObject okJson = new JSONObject(2);
        okJson.put("code", 200);
        okJson.put("msg", "Hi");
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/api/login")
                .param("phone", "123456789"));
        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        JSONObject res = JSONObject.parseObject(content);
        String token = res.getString("token");
        mvc.perform(MockMvcRequestBuilders.get("/api/test").header("token", token))
                .andExpect(MockMvcResultMatchers.content().json(okJson.toJSONString()))
                .andDo(MockMvcResultHandlers.print());
    }

}
