package com.example.texteditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ResourceUtils;

public class SnippetRepository {

    private final static String SNIPPET_LOCATION = "src/main/resources/snippets.json";

    public List<Snippet> findAll() {
        Snippet[] events;
        try {
            events = new ObjectMapper().readValue(ResourceUtils.getFile(SNIPPET_LOCATION),
                  Snippet[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.stream(events).toList();
    }

    public void saveSnippet(Snippet snippet) {
        List<Snippet> updatedSnippets = new ArrayList<>(findAll());
        updatedSnippets.add(snippet);
        try {
            new ObjectMapper().writeValue(new File(SNIPPET_LOCATION), updatedSnippets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
