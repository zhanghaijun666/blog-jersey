package com.blog.socket.graph;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.simpleframework.http.socket.Session;
import org.simpleframework.http.socket.FrameChannel;
import org.simpleframework.http.socket.service.Service;
import org.slf4j.LoggerFactory;

public class GraphPlotter extends Thread implements Service {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(GraphPlotter.class);

    private final Set<FrameChannel> sockets;

    public GraphPlotter() {
        this.sockets = new CopyOnWriteArraySet<>();
    }

    @Override
    public void connect(Session connection) {
        FrameChannel socket = connection.getChannel();

        try {
            sockets.add(socket);
        } catch (Exception e) {
            LOG.info("Problem joining chat room", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
                ThreadMXBean threads = ManagementFactory.getThreadMXBean();
                MemoryUsage usage = memory.getHeapMemoryUsage();
                long time = System.currentTimeMillis();
                double count = threads.getThreadCount();
                double max = usage.getMax();
                double used = usage.getUsed();
                double free = max - used;
                String heapUsedPoint = String.format("heapUsed:%s,%s", time, used);
                String heapFreePoint = String.format("heapFree:%s,%s", time, free);
                String threadCountPoint = String.format("threadCount:%s,%s", time, count);

                for (FrameChannel socket : sockets) {
                    try {
                        socket.send(heapUsedPoint);
                        socket.send(heapFreePoint);
                        socket.send(threadCountPoint);
                    } catch (IOException e) {
                        sockets.remove(socket);
                        LOG.info("Problem sending point", e);
                    }
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                LOG.info("Could not send point", e);
            }
        }
    }
}
