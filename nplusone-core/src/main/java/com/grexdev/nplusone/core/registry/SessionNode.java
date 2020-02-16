package com.grexdev.nplusone.core.registry;

import com.grexdev.nplusone.core.frame.FrameExtract;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Getter
@RequiredArgsConstructor(staticName = "of")
public class SessionNode {

    private final List<OperationNode> operations = new ArrayList<>();

    private final List<FrameExtract> callFrames;

    // TODO: refactor
    public void addStatement(String sql, List<FrameExtract> operationCallFrames) {
        if (startsWith(callFrames)) {
            List<FrameExtract> callFrames = operationCallFrames.subList(this.callFrames.size(), operationCallFrames.size());

            getLastOperationNode()
                    .filter(operationNode -> operationNode.hasCallFrames(callFrames))
                    .ifPresentOrElse(
                            addStatementToLastOperation(sql),
                            addStatementToNewOperation(sql, callFrames));

        } else {
            log.warn("Unable to match session and operation call frames");
        }
    }

    private Consumer<OperationNode> addStatementToLastOperation(String sql) {
        return operationNode -> operationNode.addStatement(sql);
    }

    private Runnable addStatementToNewOperation(String sql, List<FrameExtract> callFrames) {
        return () -> {
            OperationNode operationNode = new OperationNode(new ArrayList<>(callFrames));
            operations.add(operationNode);
            operationNode.addStatement(sql);
        };
    }

    private Optional<OperationNode> getLastOperationNode() {
        return operations.isEmpty()
                ? Optional.empty()
                : Optional.of(operations.get(operations.size() - 1));
    }

    private boolean startsWith(List<FrameExtract> operationCallFrames) {
        if (operationCallFrames.size() < callFrames.size()) {
            return false;
        }

        for (int i = 0; i < callFrames.size(); i++) {
            if (!callFrames.get(i).equals(operationCallFrames.get(i))) {
                return false;
            }
        }

        return true;
    }
}