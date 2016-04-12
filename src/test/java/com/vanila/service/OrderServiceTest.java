package com.vanila.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.vanila.logic.OrderLogic;
import com.vanila.order.data.OrderData.Order;

public class OrderServiceTest {
    private OrderService  orderService = null;
    private Order.Builder orderBuilder = Order.newBuilder().setId("333").setApplication("Dummy description");

    @Before
    public void setup() {
        OrderLogic mockOrderLogic = mock(OrderLogic.class);
        when(mockOrderLogic.getOrderById("333")).thenReturn(orderBuilder.build());
        // this is using mock order logic
        orderService = new OrderService(mockOrderLogic);

        // this is using real connection to database
        // orderService = new OrderService();
    }

    @Test
    public void testGetOrderById() {
        String orderId = "255";
        Order resultNotExist = orderService.getOrderById(orderId);
        assertNull(resultNotExist);

        Order resultExist = orderService.getOrderById("333");
        assertNotNull(resultExist);
        assertEquals(resultExist.getApplication(), "Dummy description");
    }
}
