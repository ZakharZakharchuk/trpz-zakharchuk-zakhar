package com.example.texteditor.command;

import com.example.texteditor.Editor;
import javax.swing.JOptionPane;
import javax.swing.text.Element;

public class RemoveBookmarkCommand extends Command {

    public RemoveBookmarkCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        int caretPosition = editor.textPane.getCaretPosition();
        Element root = editor.textPane.getDocument().getDefaultRootElement();
        int lineNumber = root.getElementIndex(caretPosition) + 1;

        if (editor.bookmarks.contains(lineNumber)) {
            editor.bookmarks.remove(Integer.valueOf(lineNumber));
            editor.lineNumbers.updateLineNumbers();
            JOptionPane.showMessageDialog(null, "Bookmark removed from line " + lineNumber);
        } else {
            JOptionPane.showMessageDialog(null, "No bookmark found at line " + lineNumber);
        }
        return false;
    }

}
