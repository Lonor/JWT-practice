package com.springboot.lawrence.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.lawrence.jwt.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lawrence
 * @date 2020/4/20
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public JSONObject test() {
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("code", 200);
        jsonObject.put("msg", "Hi");
        return jsonObject;
    }

    @GetMapping("/user")
    public JSONObject getId(String token) {
        // 这里可以再写一个拦截器+自定义注解去从 header 中取出 userId，就不用在参数里再传 token 了。
        JSONObject jsonObject = new JSONObject(2);
        jsonObject.put("code", 200);
        jsonObject.put("msg", JwtUtil.getUserIdFromJwtToken(token));
        return jsonObject;
    }

    @GetMapping(value = "/login")
    public JSONObject login(HttpServletRequest request, HttpServletResponse response) {
        String phone = request.getParameter("phone");
        String password = request.getParameter("code");
        // 这个步骤就是获取 user 的全部信息不重要，直接忽略
        Long userId = Long.parseLong(phone);
        String token = createPayLoad(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return jsonObject;
    }

    /**
     * JWT的组成：Header + payload + signature
     * Payload(载荷)的组成信息，私有声明(标准中注册的声明和公共的声明并未使用)
     *
     * @param userId 用户id
     * @return token
     */
    private String createPayLoad(Long userId) {
        Map<String, Object> payload = new HashMap<>(4);
        Date date = new Date();
        // 用户id
        payload.put("uid", String.valueOf(userId));
        // 生成时间:当前
        payload.put("iat", date.getTime());
        // 过期时间10分钟(单位毫秒)
        payload.put("ext", date.getTime() + 1000 * 60 * 10);
        return JwtUtil.createToken(payload);
    }
}
