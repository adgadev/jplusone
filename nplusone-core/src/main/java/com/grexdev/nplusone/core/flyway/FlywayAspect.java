package com.grexdev.nplusone.core.flyway;

import com.grexdev.nplusone.core.tracking.TrackingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class FlywayAspect {

    private final TrackingContext trackingContext;

    @Around("execution(* org.flywaydb.core.Flyway.*(..))")
    public Object interceptFlywayOperation(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        try {
            trackingContext.disableRecording();
            return thisJoinPoint.proceed();
        } finally {
            trackingContext.enableRecording();
        }
    }

    // TODO: intercept liquidbase calls
}
