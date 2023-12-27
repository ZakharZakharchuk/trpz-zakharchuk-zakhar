package com.example.texteditor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class HelpService {

    public void openNewWindow(String apiUrl) throws IOException {
        JFrame newWindow = new JFrame("HTML Content");
        newWindow.setSize(800, 600);

        JEditorPane newEditorPane = new JEditorPane();
        newEditorPane.setContentType("text/html");
        newEditorPane.setEditable(false);

        makeRestCall(apiUrl, newEditorPane);

        newWindow.add(new JScrollPane(newEditorPane));

        newWindow.setVisible(true);
    }

    private void makeRestCall(String apiUrl, JEditorPane targetEditorPane) {
        try {
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
                    targetEditorPane.setText(content.toString());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                  "Connection failed. Startup your microservice ", "Error",
                  JOptionPane.ERROR_MESSAGE);
        }
    }
}
