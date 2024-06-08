package com.cn.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cn.domain.SysUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String APP_SECRET = "hgdfosughdosufghuoisnvin";

    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,7);
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        builder.withExpiresAt(calendar.getTime());
        String token = builder.sign(Algorithm.HMAC256(APP_SECRET));
        return token;
    }

    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(APP_SECRET)).build().verify(token);
    }
}
