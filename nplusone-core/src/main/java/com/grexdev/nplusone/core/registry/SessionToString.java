package com.grexdev.nplusone.core.registry;

import com.grexdev.nplusone.core.frame.FrameExtract;

public class SessionToString {

    public static String toString(SessionNode session) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tSESSION");

        for (FrameExtract frame : session.getSessionCallFrameStack().getCallFrames()) {
            if (frame.isNotThirdPartyClass()) {
                builder.append("\n\t\t");
                builder.append(frame);
            }
        }

        for (OperationNode operation : session.getOperations()) {
            builder.append("\n\t\t\tOPERATION [" + operation.getOperationType() + "]");

            for (FrameExtract frame : operation.getCallFramesStack().getCallFrames()) {
                if (frame.isNotThirdPartyClass()) {
                    builder.append("\n\t\t\t\t");
                    builder.append(frame);
                }
            }

            for (StatementNode statement : operation.getStatements()) {
                builder.append("\n\t\t\t\t\tSTATEMENT [" + statement.getStatementType() + "] " + statement.getSql());
            }
        }

        return builder.toString();
    }
}


