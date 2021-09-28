package org.stroganoff.server;

public interface History {

    boolean putMessage(String message);

    StringBuffer getHistoryMessage(int count);
}
