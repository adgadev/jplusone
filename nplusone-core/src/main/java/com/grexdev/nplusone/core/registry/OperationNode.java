package com.grexdev.nplusone.core.registry;

import com.grexdev.nplusone.core.frame.FrameExtract;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OperationNode {

    enum OperationType { IMPLICIT, EXPLICIT}

    private final List<StatementNode> statements = new ArrayList<>();

    private final List<FrameExtract> callFrames;

    private final OperationType operationType;

    public OperationNode(List<FrameExtract> callFrames) {
        this.callFrames = callFrames;
        this.operationType = resolveOperationType(callFrames);
    }

    void addStatement(String sql) {
        StatementNode statement = StatementNode.fromSql(sql);
        statements.add(statement);
    }

    boolean hasCallFrames(List<FrameExtract> callFrames) {
        return this.callFrames.equals(callFrames);
    }

    private OperationType resolveOperationType(List<FrameExtract> callFrames) {
        // TODO: implicit
        return OperationType.IMPLICIT;
    }

}
