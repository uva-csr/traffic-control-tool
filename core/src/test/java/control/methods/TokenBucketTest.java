package control.methods;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TokenBucketTest {

    @Test
    public void testTokenBucketMethod() throws InterruptedException {
        Method method = new TokenBucket(5, 10_000);

        // Test within the token limit
        for (int i = 0; i < 5; i++) {
            assertTrue(method.control());
            Thread.sleep(200);
        }

        assertFalse(method.control());
        assertFalse(method.control());

        Thread.sleep(10_001);
        assertTrue(method.control());
        assertFalse(method.control());

        Thread.sleep(10_001);
        assertTrue(method.control());
        assertFalse(method.control());
    }
}
