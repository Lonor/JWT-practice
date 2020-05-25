package com.springboot.lawrence;

import com.springboot.lawrence.controller.TestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LawrenceApplicationTests {

    private MockMvc mvc;

    //初始化执行
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
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
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    //验证controller是否正常响应并判断返回结果是否正确
    @Test
    public void testHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/test").accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hi!")));
    }


}
