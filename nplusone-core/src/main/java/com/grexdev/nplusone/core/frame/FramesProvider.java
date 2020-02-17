package com.grexdev.nplusone.core.frame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.grexdev.nplusone.core.utils.StreamUtils.toListReversed;
import static java.lang.StackWalker.Option.SHOW_HIDDEN_FRAMES;

@Slf4j
@RequiredArgsConstructor
public class FramesProvider {

    private static final Set<Option> WALKER_OPTIONS = EnumSet.of(Option.RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES);

    private static final String[] PROXY_CLASS_NAME_MARKERS = { "$Proxy", "$HibernateProxy", "$$EnhancerBySpringCGLIB", "$$FastClassBySpringCGLIB" };

    private final FrameClassFactory frameClassFactory;

    public FramesProvider(String applicationRootPackage) {
        this.frameClassFactory = new FrameClassFactory(applicationRootPackage, PROXY_CLASS_NAME_MARKERS);
    }

    public List<FrameExtract> captureCallFrames() {
        return StackWalker.getInstance(WALKER_OPTIONS)
                .walk(this::collectFrames);
    }

    private List<FrameExtract> collectFrames(Stream<StackFrame> stream) {
        return stream
                .map(frameClassFactory::createFrameClass)
                .map(FrameExtract::new)
                .peek(appFrame -> log.info(appFrame.toString()))
                //.map(StreamUtils.currentAndPreviousElements())
                //.filter(StreamUtils.anyElementMatches())
                //.map(Tupple::getMainElement)
                .collect(toListReversed());
    }

}
