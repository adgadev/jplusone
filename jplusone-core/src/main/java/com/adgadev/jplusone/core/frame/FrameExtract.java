/*
 * Copyright (c) 2020 Adam Gaj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adgadev.jplusone.core.frame;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class FrameExtract {

    private final FrameClassKind type;

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

    public boolean isApplicationClass() {
        return type == FrameClassKind.APPLICATION_CLASS;
    }

    public boolean matchesMethodInvocation(Class<?> clazz, String methodName) {
        return clazz.isAssignableFrom(this.clazz) && this.methodName.equals(methodName);
    }

    @Override
    public String toString() {
        return fileName != null
                ? "[" + type.getLabel() + "]  " + type.getFilling() + className + '.' + methodName + "(" + fileName + ":" + lineNumber + ")"
                : "[" + type.getLabel() + "]  " + type.getFilling() + className + '.' + methodName + "(N/A)";
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
