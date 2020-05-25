package com.springboot.lawrence;

import static org.junit.Assert.*;

import com.springboot.lawrence.controller.TestController;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lawrence
 * @date 2020/5/25
 */
public class TestControllerTest {

    private TestController testController;

    @Before
    public void setUp() {
        testController = new TestController();
    }

    @Test
    public void testGetTest() {
        assertEquals("Hi!", testController.test());
    }
}
