package com.grexdev.nplusone.core.registry;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(staticName = "of")
public class SessionNode {

    private final List<OperationNode> operations = new ArrayList<>();

    private final List<CallFrame> callFrames;

}
