package com.example.texteditor.command;

import com.example.texteditor.Editor;

public class CopyCommand extends Command {

    public CopyCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        editor.clipboard = editor.textPane.getSelectedText();
        return false;
    }
}
