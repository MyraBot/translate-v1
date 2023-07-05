package bot.myra.translate;

import bot.myra.translate.utils.Utilities;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {
    private final static ConnectionString connectionString = new ConnectionString(Utilities.config.getProperty("mongoConnectionString"));
    private final static MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .retryWrites(true)
            .build();
    private final static MongoClient mongoClient = MongoClients.create(settings);
    private final static MongoDatabase database = mongoClient.getDatabase(Utilities.config.getProperty("databaseName")); // Get database
    public final static MongoCollection<Document> users = database.getCollection(Utilities.config.getProperty("collectionName")); // Users collection
}