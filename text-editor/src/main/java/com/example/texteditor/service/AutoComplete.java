package com.example.texteditor.service;

import com.example.texteditor.observer.ObserverManager;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class AutoComplete {

    private final JComboBox<String> autoCompleteComboBox;
    private final List<String> autoCompleteList;
    private final JTextPane textPane;
    private final ObserverManager observerManager;

    public AutoComplete(JTextPane textPane, ObserverManager observerManager) {
        this.textPane = textPane;
        this.observerManager = observerManager;

        this.autoCompleteList = new ArrayList<>();
        initAutoCompleteList();
        this.autoCompleteComboBox = createAutoCompleteComboBox();
        textPane.addKeyListener(createKeyListener());
        addAutoCompleteComboBoxToFrame();
    }

    private void initAutoCompleteList() {
        // Initialize your list of autocomplete suggestions
        autoCompleteList.add("class");
        autoCompleteList.add("public");
        autoCompleteList.add("void");
        autoCompleteList.add("new");
    }

    private JComboBox<String> createAutoCompleteComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(autoCompleteList.toArray(new String[0]));
        comboBox.setEditable(true);
        comboBox.setSelectedItem(null);

        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedItem() != null) {
                insertAutoCompleteText((String) comboBox.getSelectedItem());
            }
        });

        return comboBox;
    }

    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                char typedChar = e.getKeyChar();
                if (Character.isLetterOrDigit(typedChar)) {
                    showAutoCompleteDropdown();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                observerManager.notifyObservers();
            }
        };
    }

    private void showAutoCompleteDropdown() {
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
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(filteredList.toArray(new String[0]));
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

        int end = caretPosition;

        return content.substring(start + 1, end);
    }

    private void insertAutoCompleteText(String suggestion) {
        String currentWord = getCurrentWord();
        int caretPosition = textPane.getCaretPosition();
        int start = caretPosition - currentWord.length();
        textPane.select(start, caretPosition);
        textPane.replaceSelection(suggestion);
        autoCompleteComboBox.setPopupVisible(false);
    }

    private void addAutoCompleteComboBoxToFrame() {
        Container container = textPane.getParent();
        if (container instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) container;
            scrollPane.setColumnHeaderView(autoCompleteComboBox);
        }
    }
}

