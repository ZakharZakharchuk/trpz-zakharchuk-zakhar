package com.example.texteditor.service;

import java.awt.Color;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class LineNumberService extends JPanel {
    private final JTextPane textPane;
    private final List<Integer> bookmarks;

    public LineNumberService(JTextPane textPane, List<Integer> bookmarks) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.bookmarks = bookmarks;
        this.textPane = textPane;
        updateLineNumbers();

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
        });
    }

    public void updateLineNumbers() {
        removeAll();
        Element root = textPane.getDocument().getDefaultRootElement();
        int lineCount = root.getElementCount();
        int digits = (int) Math.log10(lineCount) + 1;

        for (int i = 1; i <= lineCount; i++) {
            JLabel lineNumberLabel = new JLabel(String.format("%" + digits + "d", i));
            if (bookmarks.contains(i)) {
                lineNumberLabel.setForeground(Color.BLUE);
            }
            add(lineNumberLabel);
        }
        revalidate();
        repaint();
    }
}
