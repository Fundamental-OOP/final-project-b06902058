package middleware;

import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AsyncMessageHandler {
    BlockingQueue<KeyEvent> blockingQueue = new LinkedBlockingDeque<KeyEvent>();

    public KeyEvent consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public void produce(KeyEvent keyEvent) {
        blockingQueue.add(keyEvent);
    }

    public void clear() {
        blockingQueue.clear();
    }
}
