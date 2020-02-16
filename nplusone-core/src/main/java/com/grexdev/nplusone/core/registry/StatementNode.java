package com.grexdev.nplusone.core.registry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.grexdev.nplusone.core.registry.StatementNode.StatementType.READ;
import static com.grexdev.nplusone.core.registry.StatementNode.StatementType.WRITE;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementNode {

    enum StatementType { READ, WRITE }

    private final String sql;

    private final StatementType statementType;

    public static StatementNode fromSql(String sql) {
        StatementType type = sql.startsWith("SELECT") || sql.startsWith("select") ? READ : WRITE;
        return new StatementNode(formatSql(sql), type);
    }

    private static String formatSql(String sql) {
        // TODO: implement
        return sql;
    }

}
