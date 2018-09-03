package com.gegaojian.girl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut(value = "execution(public * com.gegaojian.girl.controller.*.*(..))")
    public void controllerMethods(){

    }

    @Before("controllerMethods()")
    public void beforeLog(JoinPoint joinPoint){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // url
        logger.info("url = {}", request.getRequestURL());

        // method
        logger.info("method = {}", request.getMethod());

        // ip
        logger.info("ip = {}", request.getRemoteAddr());

        // 类方法
        logger.info("class_method = {}", joinPoint.getSignature().getDeclaringType() + "." + joinPoint.getSignature().getName());

        // 参数
        logger.info("args = {}", joinPoint.getArgs());
    }


    @AfterReturning(pointcut = "controllerMethods()", returning = "object")
    public void AfterControllerMethods(Object object){
        logger.info("response = {}", object.toString());
    }
}
