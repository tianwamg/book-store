package com.cn.aop;

import com.cn.request.CommonRequest;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

@Aspect
@Component
public class UserInfoAop {

    /**
     *
     * @param pjp
     * @return
     *
     * @within 和 @target:带有相应标注的所有类的任意方法，比如@Transactional
     * @annotation:带有相应标注的任意方法，比如@Transactional
     * @within和@target针对类的注解，@annotation针对方法的注解
     *
     * @args:参数带有相应标注的任意方法，比如@Transactiona
     */
    @SneakyThrows
    @Around(value = "@within(org.springframework.web.bind.annotation.RestController)")
    public Object around(ProceedingJoinPoint pjp){
        MethodSignature methodSignature =  (MethodSignature)pjp.getSignature();
        HandlerMethod handlerMethod = new HandlerMethod(pjp.getTarget(),methodSignature.getMethod());
        String name = handlerMethod.getMethod().getName();
        if(!name.equals("login") || !name.equals("register")){
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            fillParamValueWithId(methodParameters,pjp.getArgs());
        }
        Object result = pjp.proceed();
        return result;

    }

    public void fillParamValueWithId(MethodParameter[] methodParameters,Object[] args){
        for(MethodParameter methodParameter:methodParameters){
            Object obj = args[methodParameter.getParameterIndex()];
            if(obj instanceof CommonRequest){
                CommonRequest request = (CommonRequest) obj;
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                Long userId = (Long) servletRequestAttributes.getAttribute("userId",0);
                request.setUserId(userId);
            }
        }
    }
}
