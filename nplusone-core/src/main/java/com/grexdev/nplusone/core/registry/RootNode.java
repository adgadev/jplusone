package com.grexdev.nplusone.core.registry;

import java.util.ArrayList;
import java.util.List;

public class RootNode {

    private final List<SessionNode> sessions = new ArrayList<>();

    public void addSession(SessionNode session) {
        sessions.add(session);
    }

}
