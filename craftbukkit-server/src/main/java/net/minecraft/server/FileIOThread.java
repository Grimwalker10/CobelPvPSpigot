package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileIOThread implements Runnable {

    public static final FileIOThread a = new FileIOThread();
    private List b = Collections.synchronizedList(new ArrayList());
    private volatile long c;
    private volatile long d;
    private volatile boolean e;

    private FileIOThread() {
        Thread thread = new Thread(this, "File IO Thread");

        thread.setPriority(1);
        thread.start();
    }

    public void run() {
        while (true) {
    		this.b();
        }
    }

    private void b() {
        for (int i = 0; i < this.b.size(); ++i) {
            IAsyncChunkSaver iasyncchunksaver = (IAsyncChunkSaver) this.b.get(i);
            boolean flag = iasyncchunksaver.c();

            if (!flag) {
                this.b.remove(i--);
                ++this.d;
            }

            // CobelPvP - don't sleep
            /*
            try {
                Thread.sleep(this.e ? 0L : 10L);
            } catch (InterruptedException interruptedexception) {
                interruptedexception.printStackTrace();
            }
            */
        }

        if (this.b.isEmpty()) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException interruptedexception1) {
                interruptedexception1.printStackTrace();
            }
        }
    }

    public void a(IAsyncChunkSaver iasyncchunksaver) {
        if (!this.b.contains(iasyncchunksaver)) {
            ++this.c;
            this.b.add(iasyncchunksaver);
        }
    }

    public void a() throws InterruptedException {
        this.e = true;

        while (this.c != this.d) {
            Thread.sleep(10L);
        }

        this.e = false;
    }

    // CobelPvP start
    public boolean isDone() {
        return this.c == this.d;
    }

    public void setNoDelay(boolean active) {
        this.e = active;
    }
    // CobelPvP end
}
