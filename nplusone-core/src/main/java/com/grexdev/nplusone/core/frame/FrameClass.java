package com.grexdev.nplusone.core.frame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.StackWalker.StackFrame;

@Getter
@RequiredArgsConstructor(staticName = "of")
class FrameClass {

    private final FrameClassKind frameClassKind;

    private final Class<?> frameClass;

    private final StackFrame stackFrame;

}
