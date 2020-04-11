package com.grexdev.jplusone.core.proxy.datasource;

import com.grexdev.jplusone.core.proxy.StateListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.sql.Connection;

@Slf4j
@Aspect
@RequiredArgsConstructor
/**
 * This class is needed just because spring cloud's RefreshAutoConfiguration expects dataSource bean to be of type
 * com.zaxxer.hikari.HikariDataSource instead of javax.sql.DataSource if hikariCP pool is used, resulting in ClassCastException
 */
public class HikariDataSourceAspect {

    private final StateListener stateListener;

    @Around("execution(* com.zaxxer.hikari.HikariDataSource.getConnection(..))")
    public Object interceptGetConnection(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Connection connection = (Connection) thisJoinPoint.proceed();
        return new ConnectionProxy(connection, stateListener);
    }
}
