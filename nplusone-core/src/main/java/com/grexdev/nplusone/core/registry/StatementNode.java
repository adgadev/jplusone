package com.grexdev.nplusone.core.registry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.grexdev.nplusone.core.registry.StatementNode.StatementType.READ;
import static com.grexdev.nplusone.core.registry.StatementNode.StatementType.WRITE;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StatementNode {

    private static final String SELECT_CLAUSE = "select ";

    private static final String FROM_CLAUSE = " from ";

    private static final String COLUMN_LIST_SUBSTITUTE = "[...]";

    public enum StatementType { READ, WRITE }

    private final String sql;

    private final StatementType statementType;

    public static StatementNode fromSql(String sql) {
        StatementType type = sql.startsWith(SELECT_CLAUSE) ? READ : WRITE;
        return new StatementNode(formatSql(sql, type), type);
    }

    private static String formatSql(String sql, StatementType type) {
        if (type == READ) {
            int index = sql.indexOf(FROM_CLAUSE);

            if (index >= 0) {
                return new StringBuilder()
                        .append(SELECT_CLAUSE)
                        .append(COLUMN_LIST_SUBSTITUTE)
                        .append(sql.substring(index))
                        .toString();
            }
        }

        return sql;
    }

    // TODO: format JOIN statements

}
