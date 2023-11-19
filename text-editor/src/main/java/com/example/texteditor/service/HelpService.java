package com.example.texteditor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HelpService {
    public void openNewWindow(String apiUrl) throws IOException {
        // Create a new JFrame for displaying HTML content
        JFrame newWindow = new JFrame("HTML Content");
        newWindow.setSize(800, 600);

        // Create a new JEditorPane for the new window
        JEditorPane newEditorPane = new JEditorPane();
        newEditorPane.setContentType("text/html");
        newEditorPane.setEditable(false);

        // Make the REST call and set the HTML content
        makeRestCall(apiUrl, newEditorPane);

        // Add the newEditorPane to the new window
        newWindow.add(new JScrollPane(newEditorPane));

        // Set the new window properties
        //newWindow.setLocationRelativeTo(this);
        newWindow.setVisible(true);
    }
    private void makeRestCall(String apiUrl, JEditorPane targetEditorPane) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method to GET
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the HTML content from the response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                targetEditorPane.setText(content.toString());
            }
        } else {
            throw new IOException("HTTP GET request failed with response code: " + responseCode);
        }
    }
}
