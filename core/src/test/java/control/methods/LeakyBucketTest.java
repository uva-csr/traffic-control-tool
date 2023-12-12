package control.methods;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LeakyBucketTest {

    @Test
    public void testLeakyBucketMethod() throws InterruptedException {
        Method method = new LeakyBucket(10_000, 5);

        // Test within the token limit
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());

        // Test exceeding the token limit
        assertFalse(method.control());

        // Sleep for more than the interval to refill tokens
        Thread.sleep(10_000);

        // Test within the token limit after sleeping
        // refill one
        assertTrue(method.control());

        // Test exceeding the token limit again
        assertFalse(method.control());
    }
}
