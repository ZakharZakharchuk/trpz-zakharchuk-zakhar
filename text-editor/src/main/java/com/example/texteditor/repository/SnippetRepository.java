package com.example.texteditor.repository;

import com.example.texteditor.data.Snippet;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class SnippetRepository {

    private static final String COLLECTION_NAME = "snippets";
    private static final String DATABASE_NAME = "textEditor";
    private static final String MONGO_URL = "mongodb://localhost:27017";


    private final MongoCollection<Document> snippetCollection;
    private MongoClient mongoClient;

    public SnippetRepository() {
        initializeMongoClient();
        this.snippetCollection = initializeSnippetCollection();
    }

    private void initializeMongoClient() {
        mongoClient = MongoClients.create(MONGO_URL);
    }

    private MongoCollection<Document> initializeSnippetCollection() {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        return database.getCollection(COLLECTION_NAME);
    }

    public List<Snippet> findAll() {
        List<Snippet> snippets = new ArrayList<>();
        try (MongoCursor<Document> cursor = snippetCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                snippets.add(documentToSnippet(document));
            }
        }
        return snippets;
    }

    public void save(Snippet snippet) {
        snippetCollection.insertOne(snippetToDocument(snippet));
    }

    private Document snippetToDocument(Snippet snippet) {
        return new Document("name", snippet.getName())
              .append("code", snippet.getCode());
    }

    private Snippet documentToSnippet(Document document) {
        return Snippet.builder()
              .name(document.getString("name"))
              .code(document.getString("code"))
              .build();
    }
}
