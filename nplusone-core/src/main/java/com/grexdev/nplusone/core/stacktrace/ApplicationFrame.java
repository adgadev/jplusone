package com.grexdev.nplusone.core.stacktrace;

import com.grexdev.nplusone.core.stacktrace.StackTraceAnalyzer.FrameClassPair;

public class ApplicationFrame {

    private final Class<?> clazz;

    private final String className;

    private final String methodName;

    private final String fileName;

    private final Integer lineNumber;

    ApplicationFrame(FrameClassPair frameClassPair) {
        this.clazz = frameClassPair.getApplicationClass();
        this.className = this.clazz.getName();
        this.methodName = frameClassPair.getStackFrame().getMethodName();
        this.fileName = frameClassPair.getStackFrame().getFileName();
        this.lineNumber = frameClassPair.getStackFrame().getLineNumber();
    }

    @Override
    public String toString() {
        return fileName != null
                ? className + '.' + methodName + "(" + fileName + ":" + lineNumber + ")"
                : className + '.' + methodName + "(N/A)";
    }
}
