package com.example.texteditor.service;

import com.example.texteditor.Editor;
import com.example.texteditor.ExceptionHandler;
import com.example.texteditor.observer.ObserverManager;
import com.example.texteditor.strategy.ASCIIStrategy;
import com.example.texteditor.strategy.EncodingStrategy;
import com.example.texteditor.strategy.ISOStrategy;
import com.example.texteditor.strategy.UTFStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileService {

    private final Editor editor;

    public FileService(Editor editor) {
        this.editor = editor;
    }

    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getPath();
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(editor.textPane.getText());
                writer.close();
                JOptionPane.showMessageDialog(null, "Document saved successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving the document.");
            }
        }
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String encoding = new InputStreamReader(
                      new FileInputStream(selectedFile)).getEncoding();
                byte[] fileBytes = readBytesFromFile(selectedFile);
                EncodingStrategy encodingStrategy = switch (encoding) {
                    case "UTF8" -> new UTFStrategy();
                    case "US-ASCII" -> new ASCIIStrategy();
                    case "ISO-8859-1" -> new ISOStrategy();
                    default -> throw new IOException("Unsupported encoding");
                };
                String text = encodingStrategy.encode(fileBytes);
                editor.textPane.setText(text);
                new ObserverManager().notifyObservers();
            } catch (IOException ex) {
                new ExceptionHandler().showError("Error opening the document.");
            }
        }
    }

    private byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        return bytes;
    }
}
