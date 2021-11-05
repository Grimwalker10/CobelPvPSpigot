package com.cobelpvp;

import com.cobelpvp.pathsearch.PathSearchThrottlerThread;
import com.cobelpvp.pathsearch.jobs.PathSearchJob;
import com.cobelpvp.utils.PlayerDataSaveJob;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadingManager {

    private final Logger log = LogManager.getLogger();
    private ExecutorService nbtFileService = Executors.newSingleThreadExecutor(new NamePriorityThreadFactory(Thread.NORM_PRIORITY - 2, "mSpigot_NBTFileSaver"));
    private static ThreadingManager instance;
    private PathSearchThrottlerThread pathSearchThrottler;
    private ScheduledExecutorService timerService = Executors.newScheduledThreadPool(1, new NamePriorityThreadFactory(Thread.NORM_PRIORITY + 2, "mSpigot_TimerService"));
    private TickCounter tickCounter = new TickCounter();

    public ThreadingManager() {
        instance = this;
        this.pathSearchThrottler = new PathSearchThrottlerThread(2);
        this.timerService.scheduleAtFixedRate(this.tickCounter, 1, 1000, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        this.pathSearchThrottler.shutdown();
        this.nbtFileService.shutdown();
        this.timerService.shutdown();
        while(!this.nbtFileService.isTerminated()) {
            try {
                if(!this.nbtFileService.awaitTermination(3, TimeUnit.MINUTES)) {
                    log.warn("mSpigot is still waiting for NBT Files to be saved.");
                }
            } catch(InterruptedException e) {}
        }
    }

    public static void saveNBTPlayerDataStatic(PlayerDataSaveJob savejob) {
        instance.nbtFileService.execute(savejob);
    }

    public static void saveNBTFileStatic(NBTTagCompound compound, File file) {
        instance.saveNBTFile(compound, file);
    }

    public void saveNBTFile(NBTTagCompound compound, File file) {
        this.nbtFileService.execute(new NBTFileSaver(compound, file));
    }

    private class NBTFileSaver implements Runnable {

        private NBTTagCompound compound;
        private File file;

        public NBTFileSaver(NBTTagCompound compound, File file) {
            this.compound = compound;
            this.file = file;
        }

        public void run() {
            FileOutputStream fileoutputstream = null;
            try {
                fileoutputstream = new FileOutputStream(this.file);
                NBTCompressedStreamTools.a(this.compound, (OutputStream) fileoutputstream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(fileoutputstream != null) {
                    try {
                        fileoutputstream.close();
                    } catch (IOException e) {}
                }
            }
            this.compound = null;
            this.file = null;
        }
    }

    public static boolean queuePathSearch(PathSearchJob pathSearchJob) {
        return instance.pathSearchThrottler.queuePathSearch(pathSearchJob);
    }

    public class TickCounter implements Runnable {

        private ArrayDeque<Integer> ticksPerSecond;
        private AtomicInteger ticksCounter;

        public TickCounter() {
            this.ticksPerSecond = new ArrayDeque<Integer>();
            this.ticksCounter = new AtomicInteger(0);
        }

        @Override
        public void run() {
            int lastCount = this.ticksCounter.getAndSet(0);
            synchronized(this.ticksPerSecond) {
                this.ticksPerSecond.addLast(lastCount);
                if(this.ticksPerSecond.size() > 30) {
                    this.ticksPerSecond.removeFirst();
                }
            }
        }

        public void increaseTickCounter() {
            this.ticksCounter.incrementAndGet();
        }

        public Integer[] getTicksPerSecond() {
            synchronized(this.ticksPerSecond) {
                return this.ticksPerSecond.toArray(new Integer[0]);
            }
        }
    }

    public static TickCounter getTickCounter() {
        return instance.tickCounter;
    }
}
