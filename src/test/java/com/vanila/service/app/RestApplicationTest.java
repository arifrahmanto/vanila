package com.vanila.service.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.vanila.logic.OrderLogic;
import com.vanila.service.OrderService;

public class RestApplicationTest {

    @Test
    public void testGetClasses() throws Exception {
        // check the application classes are registered as JAX-RS services
        RestApplication restApplication = new RestApplication();
        Set<Class<?>> classes = restApplication.getClasses();

        assertFalse(classes.contains(OrderLogic.class));
        assertTrue(classes.contains(OrderService.class));
    }
}
