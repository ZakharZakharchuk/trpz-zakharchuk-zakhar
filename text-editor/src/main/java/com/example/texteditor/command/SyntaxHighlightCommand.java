package com.example.texteditor.command;

import com.example.texteditor.Editor;
import javax.swing.text.SimpleAttributeSet;

public class SyntaxHighlightCommand extends Command {

    private final int start;
    private final int length;
    private final SimpleAttributeSet attributes;

    public SyntaxHighlightCommand(Editor editor, int start, int length,
          SimpleAttributeSet attributes) {
        super(editor);
        this.start = start;
        this.length = length;
        this.attributes = attributes;
    }

    public boolean execute() {
        editor.textPane.getStyledDocument().setCharacterAttributes(start, length, attributes, true);
        return false;
    }
}
