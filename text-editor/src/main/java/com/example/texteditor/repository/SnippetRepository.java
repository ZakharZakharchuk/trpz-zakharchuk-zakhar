package com.example.texteditor.repository;

import com.example.texteditor.data.Snippet;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class SnippetRepository {

    private final MongoCollection<Document> snippetCollection;

    public SnippetRepository() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("textEditor");
        this.snippetCollection = database.getCollection("snippets");
    }

    public List<Snippet> findAll() {
        List<Snippet> snippets = new ArrayList<>();
        FindIterable<Document> documents = snippetCollection.find();

        try (MongoCursor<Document> cursor = documents.iterator()) {
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
