package control.methods;

public class TokenBucket implements Method {

    private int tokens;
    private final int maxTokens;
    private final long tokenFillInterval;
    private long lastFillTime;

    public TokenBucket(int maxTokens, long tokenFillInterval) {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.tokenFillInterval = tokenFillInterval;
        this.lastFillTime = System.currentTimeMillis();
    }

    @Override
    public boolean control() {
        long currentTime = System.currentTimeMillis();

        // Calculate the number of tokens that should be added since the last request
        long elapsed = currentTime - this.lastFillTime;
        int tokensToAdd = (int) (elapsed / this.tokenFillInterval);
        this.lastFillTime = currentTime;
        System.out.println("tokens to add = "  + tokensToAdd);

        this.tokens = Math.min(this.tokens + tokensToAdd, this.maxTokens);
        System.out.println("tokens= "  + tokens);

        if (this.tokens > 0) {
            // If there are tokens available, consume one and allow the request
            this.tokens--;
            return true;
        } else {
            // No tokens available, reject the request
            return false;
        }
    }
}

