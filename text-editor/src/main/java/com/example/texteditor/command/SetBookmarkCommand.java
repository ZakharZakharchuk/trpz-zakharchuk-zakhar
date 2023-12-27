package com.example.texteditor.command;

import com.example.texteditor.Editor;
import javax.swing.JOptionPane;
import javax.swing.text.Element;

public class SetBookmarkCommand extends Command {

    public SetBookmarkCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        int caretPosition = editor.textPane.getCaretPosition();
        Element root = editor.textPane.getDocument().getDefaultRootElement();
        int lineNumber = root.getElementIndex(caretPosition) + 1;

        if (!editor.bookmarks.contains(lineNumber)) {
            editor.bookmarks.add(lineNumber);
            editor.lineNumbers.updateLineNumbers();
            JOptionPane.showMessageDialog(null, "Bookmark set at line " + lineNumber);
        } else {
            JOptionPane.showMessageDialog(null, "Bookmark already exists at line " + lineNumber);
        }

        return false;
    }

}
