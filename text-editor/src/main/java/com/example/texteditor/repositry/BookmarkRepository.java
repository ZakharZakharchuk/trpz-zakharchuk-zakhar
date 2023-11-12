package com.example.texteditor.repositry;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.ResourceUtils;

public class BookmarkRepository {
    private final static String SNIPPET_LOCATION = "src/main/resources/bookmarks.json";

    public List<Integer> findAll() {
        Integer[] bookmarks;
        try {
            bookmarks = new ObjectMapper().readValue(ResourceUtils.getFile(SNIPPET_LOCATION),
                  Integer[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.stream(bookmarks).toList();
    }

    public void saveSnippet(Integer bookmark) {
        List<Integer> updatedBookmarks = new ArrayList<>(findAll());
        updatedBookmarks.add(bookmark);
        try {
            new ObjectMapper().writeValue(new File(SNIPPET_LOCATION), updatedBookmarks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeBookmark(Integer bookmark) {
        List<Integer> updatedBookmarks = new ArrayList<>(findAll());
        updatedBookmarks.remove(bookmark);
        try {
            new ObjectMapper().writeValue(new File(SNIPPET_LOCATION), updatedBookmarks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
