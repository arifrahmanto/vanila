package com.vanila.logic;

import java.util.List;

import com.google.inject.Inject;
import com.vanila.dataaccess.OrderDataAccess;
import com.vanila.dataaccess.OrderDataAccessMongo;
import com.vanila.order.data.OrderData.Order;
import com.vanila.order.data.OrderData.OrderList;

public class OrderLogic {

    private OrderDataAccess orderDataAccess;

    @Inject
    public OrderLogic(OrderDataAccessMongo orderDataAccess) {
        this.orderDataAccess = orderDataAccess;
    }

    public String addOrderData(Order newData) {
        return orderDataAccess.save(newData);
    }

    public List<Order> findByCriteria(String value, String fieldName) {
        return orderDataAccess.find(value, fieldName);
    }

    public OrderList getAllOrder() {
        List<Order> allData = orderDataAccess.findAll();
        OrderList.Builder orderListBuilder = OrderList.newBuilder();
        for (Order oneOrder : allData) {
            orderListBuilder.addOrders(
                    Order.newBuilder().setId(oneOrder.getId()).setApplication(oneOrder.getApplication()).build());
        }

        return orderListBuilder.build();
    }

    public Order getOrderById(String id) {
        return orderDataAccess.findOne(id);
    }
    
    public String deleteOne(String id){
    	return orderDataAccess.deleteOne(id);
    }
}
