package com.springboot.lawrence.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.springboot.lawrence.jwt.JwtUtil;
import com.springboot.lawrence.jwt.TokenState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lawrence
 * @date 2020/4/22
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtInterceptor.class);

    private void output(JSONObject jsonObject, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(Objects.requireNonNull(jsonObject.toJSONString()));
        out.flush();
        out.close();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 前段ajax自定义headers字段，会出现了option请求，在GET请求之前。
        // 所以应该把他过滤掉，以免影响服务。但是不能返回false，如果返回false会导致后续请求不会继续。
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        //从请求头中获取token
        String token = request.getHeader("token");
        Map<String, Object> resultMap = JwtUtil.validToken(token);
        TokenState state = TokenState.getTokenState((String) resultMap.get("state"));
        switch (state) {
            case VALID:
                //　取出payload中数据，放到request作用域中
                request.setAttribute("data", resultMap.get("data"));
                return true;
            case EXPIRED:
            case INVALID:
                LOGGER.warn("无效token");
                //JsonData是返回给前端的json格式(不重要)
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 2000);
                jsonObject.put("msg", "您的token不合法或者过期了，请重新登陆");
                output(jsonObject, response);
                break;
            default:
                break;
        }
        return false;
    }
}
