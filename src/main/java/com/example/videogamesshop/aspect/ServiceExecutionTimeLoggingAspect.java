package com.example.videogamesshop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceExecutionTimeLoggingAspect {

    @Around("within(com.example.videogamesshop.service..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startedAt = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - startedAt;
            log.info("Service method {}.{} completed in {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = System.currentTimeMillis() - startedAt;
            log.warn("Service method {}.{} failed after {} ms: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    durationMs,
                    ex.getClass().getSimpleName());
            throw ex;
        }
    }
}
