package com.example.texteditor.command;

import com.example.texteditor.Editor;

public class CutCommand extends Command{
    public CutCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        if (editor.textPane.getSelectedText().isEmpty()) return false;

        backup();
        String source = editor.textPane.getText();
        editor.clipboard = editor.textPane.getSelectedText();
        editor.textPane.setText(cutString(source));
        return true;
    }

    private String cutString(String source) {
        String start = source.substring(0, editor.textPane.getSelectionStart());
        String end = source.substring(editor.textPane.getSelectionEnd());
        return start + end;
    }
}
