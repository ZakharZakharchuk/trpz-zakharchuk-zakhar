package com.example.texteditor.command;

import com.example.texteditor.Editor;

public class CommentUncommentCommand extends Command {

    public CommentUncommentCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        backup();
        String selectedText = editor.textPane.getSelectedText();
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

            editor.textPane.replaceSelection(modifiedText.toString().trim());
            if (isCommented) {
                editor.textPane.setCaretPosition(
                      editor.textPane.getCaretPosition() - modifiedText.length());
            }
        }
        return true;
    }

}
