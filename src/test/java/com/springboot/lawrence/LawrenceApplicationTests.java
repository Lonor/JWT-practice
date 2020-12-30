package com.springboot.lawrence;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LawrenceApplicationTests {

    @Test
    void testHello(@Autowired MockMvc mvc) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 4000);
        jsonObject.put("msg", "您的token不合法或者过期了，请重新登陆");
        mvc.perform(get("/api/test")).andExpect(status().is4xxClientError()).andExpect(content().json(jsonObject.toJSONString()));
    }

    @Test
    public void login(@Autowired MockMvc mvc) throws Exception {
        JSONObject okJson = new JSONObject(2);
        okJson.put("code", 200);
        okJson.put("msg", "Hi");
        String content = mvc.perform(get("/api/login").param("phone", "123456789"))
                .andReturn().getResponse().getContentAsString();
        JSONObject res = JSONObject.parseObject(content);
        mvc.perform(get("/api/test").header("token", res.getString("token")))
                .andExpect(status().isOk())
                .andExpect(content().json(okJson.toJSONString()));

    }

}
