/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
