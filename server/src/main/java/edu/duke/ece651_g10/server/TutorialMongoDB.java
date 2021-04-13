package edu.duke.ece651_g10.server;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.bson.Document;

public class TutorialMongoDB {

  public TutorialMongoDB() {
    String connectionString = "mongodb+srv://yt136:HRXcWhSPKu2tDgzE@cluster0.jjos5.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    try (MongoClient mongoClient = MongoClients.create(connectionString)) {
      List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
      databases.forEach(db -> System.out.println(db.toJson()));
    }
  }
}












