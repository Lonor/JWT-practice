package com.springboot.lawrence.jwt;

import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Lawrence
 * @date 2020/4/22
 */
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 公共秘钥－保存在服务端，客户端是不知道该秘钥的，防止被攻击。(signature)
     */
    private static final byte[] SECRET = "1234567890qwertyuiopasdfghjklzxcvbnm".getBytes();


    /**
     * 初始化head部分的数据为(第一部分)
     * {
     * "alg":"HS256",
     * "type":"JWT"
     * }
     */
    private static final JWSHeader HEADER = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, true, null, null);

    /**
     * 生成token，该方法只在用户登录成功后调用
     *
     * @param payload Map集合，可以存储用户id，token生成时间，token过期时间等自定义字段
     * @return token字符串, 若失败则返回null
     */
    public static String createToken(Map<String, Object> payload) {
        String tokenString = null;
        // 创建一个JWS Object(第二部分)
        JWSObject jwsObject = new JWSObject(HEADER, new Payload(new JSONObject(payload).toJSONString()));
        try {
            // 将jwsObject进行HMAC签名，相当于加密(第三部分)
            jwsObject.sign(new MACSigner(SECRET));
            tokenString = jwsObject.serialize();
        } catch (JOSEException e) {
            LOGGER.error("签名失败: {}", e.getMessage());
            e.printStackTrace();
        }
        return tokenString;
    }

    /**
     * 临时的方法
     *
     * @param token token
     * @return 用户id
     */
    public static String getUserIdFromJwtToken(String token) {
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            return (String) payload.toJSONObject().get("uid");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验token是否合法，返回Map集合,集合中主要包含    state状态码   data鉴权成功后从token中提取的数据
     * 该方法在过滤器中调用，每次请求API时都校验
     *
     * @param token token
     * @return Map<String, Object>
     */
    public static Map<String, Object> validToken(String token) {
        Map<String, Object> resultMap = new HashMap<>(4);
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            // palload就是JWT构成的第二部分不过这里自定义的是私有声明(标准中注册的声明, 公共的声明)
            Payload payload = jwsObject.getPayload();
            JWSVerifier verifier = new MACVerifier(SECRET);
            if (jwsObject.verify(verifier)) {

                Map<String, Object> payloadMap = payload.toJSONObject();
                // token检验成功（此时没有检验是否过期）
                resultMap.put("state", TokenState.VALID.toString());
                // 若payload包含ext字段，则校验是否过期
                if (payloadMap.containsKey("ext")) {
                    long extTime = Long.parseLong(payloadMap.get("ext").toString());
                    long curTime = System.currentTimeMillis();
                    // 过期了
                    if (curTime > extTime) {
                        resultMap.clear();
                        resultMap.put("state", TokenState.EXPIRED.toString());
                    }
                }
                resultMap.put("data", payloadMap);
            } else {
                // 检验失败
                resultMap.put("state", TokenState.INVALID.toString());
            }
        } catch (Exception e) {
            // token格式不合法导致的异常
            // e.printStackTrace();
            resultMap.clear();
            resultMap.put("state", TokenState.INVALID.toString());
        }
        return resultMap;
    }
}
