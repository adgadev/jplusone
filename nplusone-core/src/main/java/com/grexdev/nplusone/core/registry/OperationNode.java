package com.grexdev.nplusone.core.registry;

import com.grexdev.nplusone.core.frame.FrameExtract;
import lombok.Getter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

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
        boolean isLastAppCallOnEntityClass = callFramesStack.findLastMatchingFrame(FrameExtract::isNotThirdPartyClass)
                .map(frameExtract -> frameExtract.getClazz())
                .map(clazz -> clazz.isAnnotationPresent(Entity.class))
                .orElse(FALSE);

        return isLastAppCallOnEntityClass ? OperationType.IMPLICIT : OperationType.EXPLICIT;
    }


}
