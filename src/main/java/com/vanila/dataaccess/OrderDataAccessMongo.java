package com.vanila.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vanila.order.data.OrderData.Order;

public class OrderDataAccessMongo implements OrderDataAccess {

    private MongoCollection<BasicDBObject> orderCollection;
    private MongoClient                    mongoClient;

    public OrderDataAccessMongo() {
        MongoDatabase selectedDb = MongoDBProvider.getInstance().getDatastore("sampleDb");
        orderCollection = selectedDb.getCollection("orders", BasicDBObject.class);
    }

    @Override
    public String deleteOne(String id) {
        DBObject result = orderCollection.findOneAndDelete(new Document("id", id));
        return result.toString();
    }

    @Override
    public List<Order> find(String text, String fieldName) {
        // TODO Auto-generated method stub
        // mongoClient.close();
        return null;
    }

    @Override
    public List<Order> findAll() {
        // TODO convert List<DBObject> to List<Order>?
        List<Order> result = new ArrayList<Order>();
        orderCollection.find().forEach((Block<BasicDBObject>) item -> {
            Order oneOrder = Order.newBuilder().setId(item.get("id").toString())
                    .setApplication(item.get("application").toString()).build();
            result.add(oneOrder);
        });
        
        return result;
    }

    @Override
    public Order findOne(String id) {
        DBObject result = orderCollection.find(new BasicDBObject("id", id)).first();
        System.out.println(result);
        // TODO convert DBObject to Protobuf?
        Order finalResult = null;
        if (result != null) {
            finalResult = Order.newBuilder().setId(result.get("id").toString())
                    .setApplication(result.get("application").toString()).build();
        }

        return finalResult;
    }

    @Override
    public String save(Order data) {
        // TODO convert Protobuf to DBObject?
        BasicDBObject newDBObject = new BasicDBObject("id", data.getId()).append("application", data.getApplication());
        orderCollection.insertOne(newDBObject);
        return data.getId();
    }

}
