package com.example.texteditor.service;

import com.example.texteditor.ExceptionHandler;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoService {

    public void openNewWindow(String apiUrl) {
        try {
            String content = makeRestCall(apiUrl);

            SwingUtilities.invokeLater(() -> createAndShowWindow(content));
        } catch (IOException e) {
            new ExceptionHandler().showError("Cannot get information from server, please try again");
        }
    }

    private void createAndShowWindow(String content) {
        JFrame newWindow = new JFrame("HTML Content");
        newWindow.setSize(800, 600);

        JEditorPane newEditorPane = new JEditorPane();
        newEditorPane.setContentType("text/html");
        newEditorPane.setEditable(false);
        newEditorPane.setText(content);

        newWindow.add(new JScrollPane(newEditorPane));

        newWindow.setVisible(true);
    }

    private String makeRestCall(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                  new InputStreamReader(connection.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                return content.toString();
            }
        } else {
            throw new IOException("HTTP request failed with response code: " + responseCode);
        }
    }
}
