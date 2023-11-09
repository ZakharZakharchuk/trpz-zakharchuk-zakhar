package com.example.texteditor.command;

import com.example.texteditor.Editor;
import javax.swing.text.BadLocationException;

public class PasteCommand extends Command {

    public PasteCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        if (editor.clipboard == null || editor.clipboard.isEmpty()) {
            return false;
        }

        backup();
        int caretPosition = editor.textPane.getCaretPosition();
        try {
            editor.textPane.getStyledDocument()
                  .insertString(caretPosition, editor.clipboard, null);
        } catch (BadLocationException e) {
            System.out.println("Error on paste");
        }
        return true;
    }
}
