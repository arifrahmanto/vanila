package com.vanila.dataaccess;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

public class MongoDBProvider {
    private static MongoDBProvider instance = null;

    public static synchronized MongoDBProvider getInstance() {
        if (instance == null) {
            instance = new MongoDBProvider();
        }

        return instance;
    }

    private MongoClient mongoClient;

    private MongoDBProvider() {
        try {
            if (mongoClient == null) {
                mongoClient = getClient();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MongoClient getClient() {
        return new MongoClient("localhost", 27017);
    }

    @Nullable
    public MongoDatabase getDatastore(@NotNull String dbName) {
        MongoDatabase ds = mongoClient.getDatabase(dbName);
        return ds;
    }
}
