package com.example.kursach.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@Component
@Slf4j
@Aspect
public class ControllerLogAspect {

    @Pointcut("execution(public * com.example.kursach.controller..*(..))")
    public void callController() { }

    @Before("callController()")
    public void beforeCallController(JoinPoint joinPoint) {
        List<String> args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .toList();
        String  str = joinPoint.getThis().toString();
        log.info("Call {} with args {} from {}", joinPoint.getSignature().getName(), args, str.substring(0, str.indexOf("@")));
    }

    @AfterReturning(value = "callController()", returning ="object")
    public void afterCallController(JoinPoint joinPoint, ResponseEntity<?> object) {
        log.info("Call {} with return {}", joinPoint.getSignature().getName(), object.getBody());
    }
}
