package control.methods;
import java.util.*;

public class SlidingWindow implements Method {

    private final long windowSizeInMillis;
    private LinkedList<Long> requestTimes;
    private final int maxRequests;

    public SlidingWindow(long windowSizeInMillis, int maxRequests) {
        this.windowSizeInMillis = windowSizeInMillis;
        this.requestTimes = new LinkedList<>();
        this.maxRequests = maxRequests;
    }

    @Override
    public boolean control() {
        long currentTime = System.currentTimeMillis();

        // Remove requests that are outside the sliding window
        while (!requestTimes.isEmpty() && currentTime - requestTimes.getFirst() > windowSizeInMillis) {
            requestTimes.removeFirst();
        }

        if (requestTimes.size() < maxRequests) {
            // Allow requests up to the specified limit within the window
            requestTimes.addLast(currentTime);
            return true;
        } else {
            // Request limit exceeded, reject the request
            return false;
        }
    }
}