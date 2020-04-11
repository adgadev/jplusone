package com.grexdev.jplusone.core.frame;

import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode
public class FrameExtract {

    private final FrameClassKind type;

    @Getter
    private final Class<?> clazz;

    private final String className;

    private final String methodName;

    private final String fileName;

    private final Integer lineNumber;

    FrameExtract(FrameClass frameClass) {
        this.type = frameClass.getFrameClassKind();
        this.clazz = frameClass.getFrameClass();
        this.className = frameClass.getFrameClass().getName();
        this.methodName = frameClass.getStackFrame().getMethodName();
        this.fileName = frameClass.getStackFrame().getFileName();
        this.lineNumber = frameClass.getStackFrame().getLineNumber();
    }

    public boolean isNotThirdPartyClass() {
        return type != FrameClassKind.THIRD_PARTY_CLASS;
    }

    @Override
    public String toString() {
        return fileName != null
                ? "[" + type + "] " + className + '.' + methodName + "(" + fileName + ":" + lineNumber + ")"
                : "[" + type + "] " + className + '.' + methodName + "(N/A)";
    }

    public String format() {
        String lineNumberDesc = fileName != null
                ? "(" + fileName + ":" + lineNumber + ")"
                : "";

        return type == FrameClassKind.APPLICATION_CLASS
                ? className + '.' + methodName + lineNumberDesc
                : className + '.' + methodName + " [PROXY]";
    }

    // TODO: implement cached hashcode
}
