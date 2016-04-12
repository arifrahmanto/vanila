package com.vanila.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.vanila.order.data.OrderData.Order;

public class OrderDataAccessMongoTest {

    private OrderDataAccessMongo dataAccessMongo = new OrderDataAccessMongo();

    @Test
    public void testSaveAndFindOne() {
        String expectedId = "9999";
        String expectedAppDescr = "Dummy application for test";
        try {
            Order initialResult = dataAccessMongo.findOne(expectedId);
            assertEquals(initialResult, null);
            Order.Builder orderBuilder = Order.newBuilder().setId(expectedId).setApplication(expectedAppDescr);
            String newId = dataAccessMongo.save(orderBuilder.build());
            assertFalse(newId.length() == 0);
            assertEquals(newId, expectedId);

            Order afterResult = dataAccessMongo.findOne(expectedId);
            assertFalse(afterResult == null);
            assertEquals(afterResult.getApplication(), expectedAppDescr);
        } finally {
            dataAccessMongo.deleteOne(expectedId);
        }
    }
}
