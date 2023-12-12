package control.methods;


import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SlidingWinodwTest {

    @Test
    public void testSlidingWindowMethod() throws InterruptedException {
        Method method = new SlidingWindow(1_000, 4);

        // Test within the window limit
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());

        // Test exceeding the window limit
        assertFalse(method.control());

        // Sleep for more than the window size to reset the window
        Thread.sleep(1_100);

        // Test within the window limit after sleeping
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());
        assertTrue(method.control());

        // Test exceeding the window limit again
        assertFalse(method.control());
    }
}
