package com.example.texteditor.service;

import javax.swing.*;

public class MacroService {

    public void commentUncommentBlock(JTextPane textPane) {
        String selectedText = textPane.getSelectedText();
        if (selectedText != null) {
            String[] lines = selectedText.split("\n");
            StringBuilder modifiedText = new StringBuilder();
            boolean isCommented = false;

            for (String line : lines) {
                if (line.trim().startsWith("//")) {
                    modifiedText.append(line.replaceFirst("//", "")).append("\n");
                    isCommented = true;
                } else {
                    modifiedText.append("//").append(line).append("\n");
                }
            }

            textPane.replaceSelection(modifiedText.toString().trim());
            if (isCommented) {
                textPane.setCaretPosition(textPane.getCaretPosition() - modifiedText.length());
            }
        }
    }

    public void findAndReplace(JTextPane textPane, String searchText, String replaceText) {
        String text = textPane.getText();
        text = text.replace(searchText, replaceText);
        textPane.setText(text);
    }
}
