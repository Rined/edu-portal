package com.rined.portal.aspects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LogAspect {
    @Around("@annotation(Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object proceed = joinPoint.proceed();
            log.debug("Method {} completed", joinPoint.getSignature().getName());
            return proceed;
        } catch (Throwable e) {
            log.error("Method {} error {}", joinPoint.getSignature().getName(), e);
            throw e;
        }
    }
}
