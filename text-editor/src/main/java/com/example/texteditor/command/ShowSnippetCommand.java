package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.data.Snippet;
import com.example.texteditor.repository.SnippetRepository;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

public class ShowSnippetCommand extends Command {

    private final SnippetRepository snippetRepository;

    public ShowSnippetCommand(Editor editor, SnippetRepository snippetRepository) {
        super(editor);
        this.snippetRepository = snippetRepository;
    }

    @Override
    public boolean execute() {
        List<Snippet> snippets = snippetRepository.findAll();
        JDialog dialog = new JDialog(new JFrame(), "Snippets", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel snippetPanel = new JPanel(new GridLayout(0, 1));

        for (Snippet snippet : snippets) {
            addButtonForSnippet(snippetPanel, snippet.getName(), snippet.getCode());
        }
        dialog.add(snippetPanel);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        record();

        return true;
    }


    private void addButtonForSnippet(JPanel panel, String snippetName, String snippetCode) {
        JButton snippetButton = new JButton(snippetName);
        snippetButton.addActionListener(e -> {
            try {
                editor.textPane.getDocument().insertString(
                      editor.textPane.getDocument().getLength(),
                      snippetCode,
                      null
                );
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
        panel.add(snippetButton);
    }
}
