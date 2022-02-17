package com.pavell.rickAndMortyApi.aop.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class AspectConfig {

    final
    HttpServletRequest request;

    public AspectConfig(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("within(com.pavell.rickAndMortyApi.controller..*)")
    public void controllerPointcut() {

    }

    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        long start = System.currentTimeMillis();
        log.error("request={}, time={}ms, traceUID={} Exception with cause = {}",
                request.getRequestURL(),
                System.currentTimeMillis() - start,
                "1234r",
                e.getCause() != null ? e.getCause() : "NULL");
    }

    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            log.info("request={}, response={}, time={}ms, traceUID={}",
                    request.getRequestURL(),
                    result,
                    System.currentTimeMillis() - start,
                    "1234r");//TODO:replace with uid

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

}
