package com.example.texteditor;

import com.example.texteditor.data.Snippet;
import com.example.texteditor.repositry.SnippetRepository;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class SnippetEditor {
    public void createSnippetDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Create New Snippet", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridLayout(0, 1));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel codeLabel = new JLabel("Code:");
        JTextArea codeArea = new JTextArea(5, 20);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String code = codeArea.getText();

                if (!name.isEmpty() && !code.isEmpty()) {
                    Snippet newSnippet = new Snippet();
                    newSnippet.setName(name);
                    newSnippet.setCode(code);
                    SnippetRepository snippetRepository = new SnippetRepository();

                    snippetRepository.saveSnippet(newSnippet);

                    dialog.dispose();
                }
            }
        });
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(codeLabel);
        dialog.add(new JScrollPane(codeArea));
        dialog.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    public static void showSnippetDialog(JFrame parentFrame, StyledDocument doc) {
        SnippetRepository snippetRepository = new SnippetRepository();
        List<Snippet> snippets = snippetRepository.findAll();
        JDialog dialog = new JDialog(parentFrame, "Snippets", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel snippetPanel = new JPanel(new GridLayout(0, 1));

        for (Snippet snippet : snippets) {
            addButtonForSnippet(snippetPanel, snippet.getName(), doc, snippet.getCode());
        }
        dialog.add(snippetPanel);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private static void addButtonForSnippet(JPanel panel, String snippetName, StyledDocument
          doc,
          String snippetCode) {
        JButton snippetButton = new JButton(snippetName);
        snippetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doc.insertString(doc.getLength(), snippetCode, null);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(snippetButton);
    }
}
