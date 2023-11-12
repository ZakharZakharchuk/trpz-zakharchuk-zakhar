package com.example.texteditor.command;

import com.example.texteditor.Editor;

public abstract class Command {

    public Editor editor;
    private String backup;

    Command(Editor editor) {
        this.editor = editor;
    }

    void backup() {
        backup = editor.textPane.getText();
    }

    public void undo() {
        editor.textPane.setText(backup);
    }

    public abstract boolean execute();
}
