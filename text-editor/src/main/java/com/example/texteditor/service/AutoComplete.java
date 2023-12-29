package com.example.texteditor.service;

import com.example.texteditor.Editor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextPane;

public class AutoComplete {

    private JComboBox<String> autoCompleteComboBox;
    private List<String> autoCompleteList;
    private final JTextPane textPane;

    public AutoComplete(Editor editor) {
        this.textPane = editor.textPane;
        initAutoCompleteList();
        initAutoCompleteComboBox();

    }

    private void initAutoCompleteList() {
        autoCompleteList = Arrays.asList(
              "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
              "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
              "finally", "float", "for", "if", "implements", "import", "instanceof", "int",
              "interface", "long", "native", "new", "package", "private", "protected", "public",
              "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
              "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false",
              "null");

    }

    public JComboBox<String> getAutoCompleteComboBox() {
        return autoCompleteComboBox;
    }

    public void initAutoCompleteComboBox() {
        autoCompleteComboBox = new JComboBox<>(autoCompleteList.toArray(new String[0]));
        autoCompleteComboBox.setEditable(true);
        autoCompleteComboBox.setSelectedItem(null);

        autoCompleteComboBox.addActionListener(e -> {
            if (autoCompleteComboBox.getSelectedItem() != null) {
                insertAutoCompleteText((String) autoCompleteComboBox.getSelectedItem());
            }
        });
    }


    public void showAutoCompleteDropdown() {
        List<String> filteredList = new ArrayList<>();
        for (String suggestion : autoCompleteList) {
            if (suggestion.contains(getCurrentWord())) {
                filteredList.add(suggestion);
            }
        }
        autoCompleteComboBox.removeAllItems();
        if (filteredList.isEmpty()) {
            autoCompleteComboBox.setPopupVisible(false);
        } else {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
                  filteredList.toArray(new String[0]));
            autoCompleteComboBox.setModel(model);
            autoCompleteComboBox.setPopupVisible(true);
        }
    }

    private String getCurrentWord() {
        String content = textPane.getText();
        int caretPosition = textPane.getCaretPosition();
        int start = caretPosition - 1;

        while (start >= 0 && Character.isLetterOrDigit(content.charAt(start))) {
            start--;
        }

        return content.substring(start + 1, caretPosition);
    }

    private void insertAutoCompleteText(String suggestion) {
        String currentWord = getCurrentWord();
        int caretPosition = textPane.getCaretPosition();
        int start = caretPosition - currentWord.length();
        textPane.select(start, caretPosition);
        textPane.replaceSelection(suggestion);
        autoCompleteComboBox.setPopupVisible(false);
    }
}

