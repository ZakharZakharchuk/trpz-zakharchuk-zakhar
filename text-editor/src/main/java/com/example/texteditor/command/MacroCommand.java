package com.example.texteditor.command;

import com.example.texteditor.Editor;

public class MacroCommand extends Command {

    private final Runnable action;

    MacroCommand(Editor editor, Runnable action) {
        super(editor);
        this.action = action;
    }

    public boolean execute() {
        action.run();
        return false;
    }
}
