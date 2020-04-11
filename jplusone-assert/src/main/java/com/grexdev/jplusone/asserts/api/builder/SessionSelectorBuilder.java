package com.grexdev.jplusone.asserts.api.builder;

public interface SessionSelectorBuilder {

    ExecutionFilterBuilder eachSession();

    ExecutionFilterBuilder firstSession();

    ExecutionFilterBuilder lastSession();

    ExecutionFilterBuilder nthSession();

    ExecutionFilterBuilder explicitSessionBoundary(SessionBoundary sessionBoundary);

    interface SessionBoundary {
        // TODO: design
        // capture start stop / everythin within lambda
    }
}
