package com.grexdev.nplusone.core.registry;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OperationNode {

    enum OperationType { IMPLICIT, EXPLICIT}

    private final List<StatementNode> statements = new ArrayList<>();

    private final FrameStack callFramesStack;

    private final OperationType operationType;

    public OperationNode(FrameStack callFramesStack) {
        this.callFramesStack = callFramesStack;
        this.operationType = resolveOperationType(callFramesStack);
    }

    void addStatement(String sql) {
        StatementNode statement = StatementNode.fromSql(sql);
        statements.add(statement);
    }

    boolean hasCallFramesStack(FrameStack callFramesStack) {
        return this.callFramesStack.equals(callFramesStack);
    }

    private OperationType resolveOperationType(FrameStack callFramesStack) {
        // TODO: implicit
        return OperationType.IMPLICIT;
    }

}
