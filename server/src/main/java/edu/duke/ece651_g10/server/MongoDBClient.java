package edu.duke.ece651_g10.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * The class to update the game and user information into database
 */
public class MongoDBClient {
  final String connectionString;

  /**
   * The constructor of the MongoDBClient
   */
  public MongoDBClient(String connectionString) {
    this.connectionString = connectionString;
  }

  /**
   * Add the Game into the database
   * 
   * @param game The game to add
   */
  public void addGame2DB(Game game) {
    try (MongoClient mongoClient = MongoClients.create(connectionString)) {
      MongoDatabase riskDB = mongoClient.getDatabase("ece651_risk");
      MongoCollection<Document> gameCollection = riskDB.getCollection("games");
      Document gameDoc = new Document("_id", new ObjectId());
      gameDoc.append("game_id", game.getGameId()).append("end_game", game.getGameEnd());
      gameCollection.insertOne(gameDoc);
    }
  }
}











