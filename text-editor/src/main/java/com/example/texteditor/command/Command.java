package com.example.texteditor.command;

import com.example.texteditor.Editor;

public abstract class Command {

    public final Editor editor;
    private String backup;

    protected Command(Editor editor) {
        this.editor = editor;
    }

    protected void backup() {
        backup = editor.textPane.getText();
    }

    public void undo() {
        editor.textPane.setText(backup);
    }

    public abstract boolean execute();
}
