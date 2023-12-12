package utils;

import system.monitors.SystemMetric;

public class ArrayForSystemMetric extends MetricArray<SystemMetric> {

    /**
     * The total bucket count is: {@code sampleCount = intervalInMs / windowLengthInMs}.
     *
     * @param sampleCount  bucket count of the sliding window
     * @param intervalInMs the total time interval of this {@link MetricArray} in milliseconds
     */
    public ArrayForSystemMetric(int sampleCount, int intervalInMs) {
        super(sampleCount, intervalInMs);
    }

    @Override
    public SystemMetric newEmptyBucket(long timeMillis) {
        return null;
    }

    @Override
    protected Window<SystemMetric> resetWindowTo(Window<SystemMetric> window, long startTime) {
        return null;
    }
}
