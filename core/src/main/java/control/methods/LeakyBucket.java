package control.methods;

public class LeakyBucket implements Method {
    private final long intervalInMillis;
    private long lastLeakTime;
    private int tokens;
    private final int maxTokens;

    public LeakyBucket(long intervalInMillis, int maxTokens) {
        this.intervalInMillis = intervalInMillis;
        this.lastLeakTime = System.currentTimeMillis();
        this.tokens = maxTokens;
        this.maxTokens = maxTokens;
    }

    @Override
    public boolean control() {
        long currentTime = System.currentTimeMillis();

        // Calculate the number of tokens leaked since the last request
        int leakedTokens = (int) ((currentTime - this.lastLeakTime) / this.intervalInMillis);
        this.tokens = Math.min(this.tokens + leakedTokens, this.maxTokens);

        if (tokens > 0) {
            // If there are tokens available, consume one and allow the request
            this.tokens--;
            this.lastLeakTime = currentTime;
            return true;
        } else {
            // No tokens available, reject the request
            return false;
        }
    }
}
