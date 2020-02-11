package com.grexdev.nplusone.core.registry;

import java.util.ArrayList;
import java.util.List;

public class OperationNode {

    enum OperationType { IMPLICIT, EXPLICIT}

    private final List<StatementNode> statements = new ArrayList<>();

    private final List<CallFrame> callFrames;

    private final OperationType operationType;

    public OperationNode(List<CallFrame> callFrames) {
        this.callFrames = callFrames;
        this.operationType = resolveOperationType(callFrames);
    }

    private OperationType resolveOperationType(List<CallFrame> callFrames) {
        // TODO: implicit
        return OperationType.IMPLICIT;
    }
}
