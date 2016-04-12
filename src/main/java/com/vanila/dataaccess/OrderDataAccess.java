package com.vanila.dataaccess;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.vanila.order.data.OrderData.Order;

@ImplementedBy(OrderDataAccessMongo.class)
public interface OrderDataAccess {

    public String deleteOne(String id);

    public List<Order> find(String text, String fieldName);

    public List<Order> findAll();

    public Order findOne(String id);

    public String save(Order data);
}