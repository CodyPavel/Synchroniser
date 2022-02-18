package com.pavell.rickAndMortyApi.aop.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Slf4j
@Aspect
@Component
public class AspectConfig {

    final HttpServletRequest request;
    final HttpServletResponse response;

    public AspectConfig(HttpServletRequest request,
                        HttpServletResponse response
    ) {
        this.request = request;
        this.response = response;
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
                response.getHeader("UID"),
                e.getCause() != null ? e.getCause() : "NULL");
    }


    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            StringBuilder builder = new StringBuilder();
            if (request.getParameterMap().size() > 0) {
                builder.append("{");
                request.getParameterMap().forEach((key, val) -> builder.append(key + "=" + Arrays.toString(val) + ", "));
                builder.delete(builder.length() - 2, builder.length()).append("}");
            }

            log.info("request={}, response={}, time={}ms, traceUID={}",
                    request.getRequestURL() + builder.toString(),
                    result,
                    System.currentTimeMillis() - start,
                    response.getHeader("UID"));

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    @Before("controllerPointcut()")
    public void UIDAround() {
        try {
            response.setHeader("UID", randomUUID().toString());
        } catch (IllegalArgumentException e) {
            log.error("Error while adding UID to response for request" + request.getRequestURL());
            throw e;
        }
    }
}
