package com.cn.config;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cn.anno.PassToken;
import com.cn.domain.SysUser;
import com.cn.request.CommonRequest;
import com.cn.util.JwtUtil;
import com.cn.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@SuppressWarnings("all")
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*if(request.getMethod().equals("OPTIONS")){
            response.setHeader("Access-Control-Allow-0rigin","*");
            response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, HEAD, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers","token");
            response.setHeader("Access-Control-Allow-Credentials","true");
            response.setStatus(HttpServletResponse.SC_OK);

        }*/

        //*表示放行所有的源，http://127.0.0.1:5504

        String profile = SpringContextUtil.getActiveProfile();

        //测试环境跳过验证
        /*if(profile !=null && profile.equals("dev")){
            return true;
        }*/
        String token = request.getHeader("token");
        if(!(handler instanceof HandlerMethod)){//不是方法
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)){
            return true;
        }
        if(StringUtils.isEmpty(token)){
            throw new LoginException("请求头丢失，请重新登录");
        }

        try {
            //从redis获取token
            String ken = redisTemplate.opsForValue().get("login-"+token);
            if(token==null || StringUtils.isEmpty(ken)){
                throw new LoginException("登录信息校验失败，请重新登录");
            }
            SysUser sysUser = JSONObject.parseObject(ken,SysUser.class);
            if(sysUser.getRoleId() >2 && sysUser.getExpireTime().getTime() < new Date().getTime()){
                throw new LoginException("用户会员已过期，请重新充值!");
            }
            request.setAttribute("userId",sysUser.getUserId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new LoginException("登录信息校验失败，请重新登录");
        }
    }
}
