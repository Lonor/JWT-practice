package com.springboot.lawrence;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class LawrenceApplicationTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    //初始化执行
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 验证controller是否正常响应并打印返回结果
     *
     * @throws Exception perform method
     */
    @Test
    public void getTest() throws Exception {
        System.out.println("Hello Test!");
        mvc.perform(MockMvcRequestBuilders.get("/api/test").accept(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    //验证controller是否正常响应并判断返回结果是否正确
    @Test
    public void testHello() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 4000);
        jsonObject.put("msg", "您的token不合法或者过期了，请重新登陆");
        mvc.perform(MockMvcRequestBuilders.get("/api/test") /* .header("token", "12345") */)
                .andExpect(MockMvcResultMatchers.content().json(jsonObject.toJSONString()));
    }


}
