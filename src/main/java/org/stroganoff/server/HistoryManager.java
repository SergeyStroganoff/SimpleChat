package org.stroganoff.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class HistoryManager implements History {
    BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(5);

    public boolean putMessage(String message) {
        if (arrayBlockingQueue.remainingCapacity() == 0) {
            arrayBlockingQueue.poll();
        }
        return arrayBlockingQueue.offer(message);
    }

    public StringBuffer getHistoryMessage(int count) {
        StringBuffer stringBuffer = new StringBuffer();
        int numberOfMessage = 0;

        for (String message : arrayBlockingQueue) {
            stringBuffer.append(message).append("\n");
            numberOfMessage++;
            if (numberOfMessage >= count) {
                break;
            }
        }
        return stringBuffer;
    }
}
