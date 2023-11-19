package com.example.texteditor.service;

import com.example.texteditor.Editor;
import javax.swing.JOptionPane;
import javax.swing.text.Element;

public class BookmarkService {

    public void setBookmark(Editor editor) {
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
    }

    public void removeBookmark(Editor editor) {
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
    }
}
