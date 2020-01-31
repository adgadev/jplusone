package com.grexdev.nplusone.core.stacktrace;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.StackWalker.StackFrame;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.lang.StackWalker.Option.SHOW_HIDDEN_FRAMES;
import static java.util.stream.Collectors.toList;

@Slf4j
public class StackTraceAnalyzer {

    //private Package rootPackage = StackTraceAnalyzer.class.getClassLoader().getDefinedPackage("com.grexdev.sandbox.demo");

    private final String applicationPackage = "com.grexdev.nplusone.test.";

    public void check() {
        List<ApplicationFrame> applicationFrames = StackWalker.getInstance(EnumSet.of(RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES))
                .walk(this::walkStackTrace);
    }

    private List<ApplicationFrame> walkStackTrace(Stream<StackFrame> stream) {
        return stream
                .map(FrameClassPair::new)
                .filter(FrameClassPair::isApplicationFrame)
                .map(ApplicationFrame::new)
                .peek(appFrame -> log.info(appFrame.toString()))
                .collect(toList());
    }

    @Getter(AccessLevel.PACKAGE)
    class FrameClassPair {

        private final StackFrame stackFrame;

        private final Class<?> applicationClass;

        private FrameClassPair(StackFrame stackFrame) {
            this.stackFrame = stackFrame;
            this.applicationClass = resolveApplicationClass(stackFrame);
        }

        boolean isApplicationFrame() {
            return applicationClass != null;
        }

        private Class<?> resolveApplicationClass(StackFrame stackFrame) {
            Class<?> frameClass = stackFrame.getDeclaringClass();

            if (isClassInsidePackage(frameClass) && isClassNotAProxy(frameClass)) {
                return frameClass;

            } else if (frameClass.getSuperclass() != null && isClassInsidePackage(frameClass.getSuperclass())) {
                return frameClass.getSuperclass();

            } else {
                return Arrays.stream(frameClass.getInterfaces())
                        .filter(this::isClassInsidePackage)
                        .filter(interfaceClass -> classContainsMethod(interfaceClass, stackFrame.getMethodName()))
                        .findFirst()
                        .orElse(null);
            }
        }

        private boolean isClassInsidePackage(Class<?> clazz) {
            return clazz.getName().startsWith(applicationPackage);
        }

        private boolean classContainsMethod(Class<?> clazz, String methodName) {
            return Arrays.stream(clazz.getMethods())
                    .anyMatch(method -> method.getName().equals(methodName));
        }

        private boolean isClassNotAProxy(Class<?> clazz) {
            return clazz.getCanonicalName().contains("$HibernateProxy") == false
                    && clazz.getCanonicalName().contains("$$EnhancerBySpringCGLIB") == false;
        }
    }

}
