package net.minecraft.optimizations.pathsearch.jobs;

import net.minecraft.optimizations.ThreadingManager;

public class PathSearchQueuingManager {

    private PathSearchJob lastQueuedJob;

    public PathSearchQueuingManager() {
        this.lastQueuedJob = null;
    }

    public synchronized boolean hasAsyncSearchIssued() {
        return this.lastQueuedJob != null;
    }

    public synchronized void queueSearch(PathSearchJob job) {
        if(ThreadingManager.queuePathSearch(job)) {
            this.lastQueuedJob = job;
        }
    }

    public synchronized void checkLastSearchResult(PathSearchJob pathSearch) {
        if(this.lastQueuedJob == pathSearch) {
            this.lastQueuedJob = null;
        }
    }
}
