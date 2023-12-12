package control.methods;

public class FixedWindow implements Method {
    private long windowStart;
    private final long windowSizeInMillis;
    private int requestCount;
    private final int maxRequests;

    public FixedWindow(long windowSizeInMillis, int maxRequests) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.windowStart = System.currentTimeMillis();
        this.requestCount = 0;
        this.maxRequests = maxRequests;
    }

    @Override
    public boolean control() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - windowStart < windowSizeInMillis) {
            // Inside the window, check if the request count exceeds the limit
            return requestCount++ < maxRequests;
        } else {
            // Outside the window, reset and allow the first request
            windowStart = currentTime;
            this.requestCount = 1;
            return true;
        }
    }
}
