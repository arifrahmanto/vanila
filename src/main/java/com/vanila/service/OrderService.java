package com.vanila.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.wink.providers.protobuf.WinkProtobufProvider;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vanila.logic.OrderLogic;
import com.vanila.order.data.OrderData.Order;
import com.vanila.order.data.OrderData.OrderList;

/**
 *
 * User: purwol Date: Feb 2, 2016 Time: 11:37:47 PM
 */

@Path("/order")
public class OrderService {

    private OrderLogic orderLogic;

    public OrderService() {
        Injector injector = Guice.createInjector();
        orderLogic = injector.getInstance(OrderLogic.class);
    }

    @Inject
    public OrderService(OrderLogic orderDAO) {
        this.orderLogic = orderDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)        
    public OrderList getOrdersJSON() {
        return orderLogic.getAllOrder();
    }
    
    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)        
    public Order getOrderJSONById(@PathParam("orderId") String orderId) {
        return orderLogic.getOrderById(orderId);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String post(Order order) {
    	return orderLogic.addOrderData(order);
    }
    
    @DELETE
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)        
    public String delete(@PathParam("orderId") String orderId) {
        return orderLogic.deleteOne(orderId);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public String put(Order order) {
    	return orderLogic.addOrderData(order);
    }
    
    @GET
    @Path("/getAll.{mediatype}")
    @Produces({ WinkProtobufProvider.PROTOBUF, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public OrderList getOrder() {
        return orderLogic.getAllOrder();
    }

    @GET
    @Path("/getById.{mediatype}")
    @Produces({ WinkProtobufProvider.PROTOBUF, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Order getOrderById(@QueryParam("orderId") String orderId) {
        return orderLogic.getOrderById(orderId);
    }
}
