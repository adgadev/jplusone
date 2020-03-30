package com.grexdev.nplusone.core.registry;

import com.grexdev.nplusone.core.frame.FrameExtract;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Getter
public class OperationNode {

    public enum OperationType {IMPLICIT_FETCH, EXPLICIT_FETCH}

    private final List<StatementNode> statements = new ArrayList<>();

    private final FrameStack callFramesStack;

    private OperationType operationType;

    private LazyInitialisation lazyInitialisation;

    public OperationNode(FrameStack callFramesStack) {
        this.callFramesStack = callFramesStack;
        this.lazyInitialisation = resolveLazyInitialisationDetails(callFramesStack);
        this.operationType = nonNull(lazyInitialisation) ? OperationType.IMPLICIT_FETCH : OperationType.EXPLICIT_FETCH;
    }

    void addStatement(String sql) {
        StatementNode statement = StatementNode.fromSql(sql);
        statements.add(statement);
    }

    void addLazyInitialisation(LazyInitialisation lazyInitialisation) {
        if (this.lazyInitialisation == null) {
            this.lazyInitialisation = lazyInitialisation;
            this.operationType = OperationType.IMPLICIT_FETCH;
        } else {
            log.warn("Details of operation lazy initialisation already captured, bug?");
        }
    }

    boolean hasCallFramesStack(FrameStack callFramesStack) {
        return this.callFramesStack.equals(callFramesStack);
    }

    private LazyInitialisation resolveLazyInitialisationDetails(FrameStack callFramesStack) {
        return callFramesStack.findLastMatchingFrame(FrameExtract::isNotThirdPartyClass)
                .map(frameExtract -> frameExtract.getClazz())
                .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                .map(Class::getCanonicalName)
                .map(LazyInitialisation::entityLazyInitialisation)
                .orElse(null);
    }
}
