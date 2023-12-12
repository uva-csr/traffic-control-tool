package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

public abstract class MetricArray<T> {
    protected int windowLengthInMs;
    protected int sampleCount;
    protected int intervalInMs;
    private double intervalInSecond;

    protected final AtomicReferenceArray<Window<T>> array;

    private final ReentrantLock updateLock = new ReentrantLock();

    /**
     * The total bucket count is: {@code sampleCount = intervalInMs / windowLengthInMs}.
     *
     * @param sampleCount  bucket count of the sliding window
     * @param intervalInMs the total time interval of this {@link MetricArray} in milliseconds
     */
    public MetricArray(int sampleCount, int intervalInMs) {

        this.windowLengthInMs = intervalInMs / sampleCount;
        this.intervalInMs = intervalInMs;
        this.intervalInSecond = intervalInMs / 1000.0;
        this.sampleCount = sampleCount;

        this.array = new AtomicReferenceArray<>(sampleCount);
    }

    public Window<T> currentWindow() {
        return currentWindow(System.currentTimeMillis());
    }

    public abstract T newEmptyBucket(long timeMillis);

    protected abstract Window<T> resetWindowTo(Window<T> window, long startTime);

    private int calculateTimeIdx(long timeMillis) {
        long timeId = timeMillis / windowLengthInMs;
        // Calculate current index so we can map the timestamp to the leap array.
        return (int)(timeId % array.length());
    }

    protected long calculateWindowStart(long timeMillis) {
        return timeMillis - timeMillis % windowLengthInMs;
    }

    public Window<T> currentWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }

        int idx = calculateTimeIdx(timeMillis);
        long windowStart = calculateWindowStart(timeMillis);

        while (true) {
            Window<T> old = array.get(idx);
            if (old == null) {
                Window<T> window = new Window<T>(windowLengthInMs, windowStart, newEmptyBucket(timeMillis));
                if (array.compareAndSet(idx, null, window)) {
                    // Successfully updated, return the created bucket.
                    return window;
                } else {
                    // Contention failed, the thread will yield its time slice to wait for bucket available.
                    Thread.yield();
                }
            } else if (windowStart == old.windowStart()) {
                return old;
            } else if (windowStart > old.windowStart()) {
                if (updateLock.tryLock()) {
                    try {
                        // Successfully get the update lock, now we reset the bucket.
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    // Contention failed, the thread will yield its time slice to wait for bucket available.
                    Thread.yield();
                }
            } else if (windowStart < old.windowStart()) {
                // Should not go through here, as the provided time is already behind.
                return new Window<T>(windowLengthInMs, windowStart, newEmptyBucket(timeMillis));
            }
        }
    }

    public Window<T> getPreviousWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        int idx = calculateTimeIdx(timeMillis - windowLengthInMs);
        timeMillis = timeMillis - windowLengthInMs;
        Window<T> wrap = array.get(idx);

        if (wrap == null || isWindowDeprecated(wrap)) {
            return null;
        }

        if (wrap.windowStart() + windowLengthInMs < (timeMillis)) {
            return null;
        }

        return wrap;
    }

    public Window<T> getPreviousWindow() {
        return getPreviousWindow(System.currentTimeMillis());
    }

    public T getWindowValue(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        int idx = calculateTimeIdx(timeMillis);

        Window<T> bucket = array.get(idx);

        if (bucket == null || !bucket.isTimeInWindow(timeMillis)) {
            return null;
        }

        return bucket.value();
    }

    public boolean isWindowDeprecated(/*@NonNull*/ Window<T> windowWrap) {
        return isWindowDeprecated(System.currentTimeMillis(), windowWrap);
    }

    public boolean isWindowDeprecated(long time, Window<T> windowWrap) {
        return time - windowWrap.windowStart() > intervalInMs;
    }

    public List<Window<T>> list() {
        return list(System.currentTimeMillis());
    }

    public List<Window<T>> list(long validTime) {
        int size = array.length();
        List<Window<T>> result = new ArrayList<Window<T>>(size);

        for (int i = 0; i < size; i++) {
            Window<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(validTime, windowWrap)) {
                continue;
            }
            result.add(windowWrap);
        }

        return result;
    }

    public List<Window<T>> listAll() {
        int size = array.length();
        List<Window<T>> result = new ArrayList<Window<T>>(size);

        for (int i = 0; i < size; i++) {
            Window<T> windowWrap = array.get(i);
            if (windowWrap == null) {
                continue;
            }
            result.add(windowWrap);
        }

        return result;
    }

    public List<T> values() {
        return values(System.currentTimeMillis());
    }

    public List<T> values(long timeMillis) {
        if (timeMillis < 0) {
            return new ArrayList<T>();
        }
        int size = array.length();
        List<T> result = new ArrayList<T>(size);

        for (int i = 0; i < size; i++) {
            Window<T> windowWrap = array.get(i);
            if (windowWrap == null || isWindowDeprecated(timeMillis, windowWrap)) {
                continue;
            }
            result.add(windowWrap.value());
        }
        return result;
    }

    Window<T> getValidHead(long timeMillis) {
        // Calculate index for expected head time.
        int idx = calculateTimeIdx(timeMillis + windowLengthInMs);

        Window<T> wrap = array.get(idx);
        if (wrap == null || isWindowDeprecated(wrap)) {
            return null;
        }

        return wrap;
    }

    public Window<T> getValidHead() {
        return getValidHead(System.currentTimeMillis());
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public int getIntervalInMs() {
        return intervalInMs;
    }

    public double getIntervalInSecond() {
        return intervalInSecond;
    }

    public long currentWaiting() {
        // TODO: default method. Should remove this later.
        return 0;
    }

    public void addWaiting(long time, int acquireCount) {
        // Do nothing by default.
        throw new UnsupportedOperationException();
    }
}
