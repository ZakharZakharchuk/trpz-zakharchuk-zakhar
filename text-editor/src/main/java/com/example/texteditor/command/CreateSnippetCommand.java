package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.data.Snippet;
import com.example.texteditor.repository.SnippetRepository;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateSnippetCommand extends Command{

    private final SnippetRepository snippetRepository;

    public CreateSnippetCommand(Editor editor, SnippetRepository snippetRepository) {
        super(editor);
        this.snippetRepository = snippetRepository;
    }

    @Override
    public boolean execute() {
        JDialog dialog = new JDialog(new JFrame(), "Create New Snippet", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridLayout(0, 1));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel codeLabel = new JLabel("Code:");
        JTextArea codeArea = new JTextArea(5, 20);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String code = codeArea.getText();

            if (!name.isEmpty() && !code.isEmpty()) {
                Snippet newSnippet = Snippet.builder()
                      .name(name)
                      .code(code)
                      .build();

                snippetRepository.save(newSnippet);

                dialog.dispose();
            }
        });
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(codeLabel);
        dialog.add(new JScrollPane(codeArea));
        dialog.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return false;
    }

}
