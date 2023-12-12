package control.methods;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedWindowTest {


    @Test
    public void testFixedWindowMethod() throws InterruptedException {
        FixedWindow method = new FixedWindow(1_000, 4);

        for (int i = 0; i < 4; i++) {
            assertTrue(method.control());
        }

        // outside the window, should be rejected
        assertFalse(method.control());
        assertFalse(method.control());

        Thread.sleep(1000);
        assertTrue(method.control());
    }
}
