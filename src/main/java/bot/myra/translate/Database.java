package bot.myra.translate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {
    private final static ConnectionString connectionString = new ConnectionString("mongodb+srv://Marian:dGP3e3Iewlqypmxq@cluster0.epzcx.mongodb.net/test?w=majority");
    private final static MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .retryWrites(true)
            .build();
    private final static MongoClient mongoClient = MongoClients.create(settings);
    private final static MongoDatabase database = mongoClient.getDatabase("MyraTranslation"); // Get database
    public final static MongoCollection<Document> users = database.getCollection("users"); // Users collection
}