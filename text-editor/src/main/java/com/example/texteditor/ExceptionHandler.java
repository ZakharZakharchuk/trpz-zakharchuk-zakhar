package com.example.texteditor;

import javax.swing.JOptionPane;

public class ExceptionHandler {
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
