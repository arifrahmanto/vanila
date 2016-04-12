package com.vanila.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.vanila.dataaccess.OrderDataAccessMongo;
import com.vanila.logic.OrderLogic;
import com.vanila.order.data.OrderData.Order;

public class OrderLogicTest {

    private OrderLogic    orderLogic       = null;
    private String        orderId          = "10101";
    private String        notExistsOrderId = "9999";
    private String        description      = "Dummy application description for test purposes";
    private Order.Builder orderBuilder     = Order.newBuilder().setId(orderId).setApplication(description);

    @Before
    public void setup() throws Exception {
        // mock the data access
        OrderDataAccessMongo mockOrderDataAccess = mock(OrderDataAccessMongo.class);
        // mock the method return result
        when(mockOrderDataAccess.save(any())).thenReturn(orderId);
        when(mockOrderDataAccess.findOne(orderId))
                .thenReturn(orderId != notExistsOrderId ? orderBuilder.build() : null);
        orderLogic = new OrderLogic(mockOrderDataAccess);
    }

    @Test
    public void testAddAndFindOneOrderData() {
        String newOrderId = orderLogic.addOrderData(orderBuilder.build());
        assertNotNull(newOrderId);
        assertEquals(newOrderId, orderId);

        Order newCreatedOrder = orderLogic.getOrderById(orderId);
        assertNotNull(newCreatedOrder);
        assertEquals(newCreatedOrder.getApplication(), description);

        Order undefinedOrder = orderLogic.getOrderById(notExistsOrderId);
        assertNull(undefinedOrder);
    }

}
