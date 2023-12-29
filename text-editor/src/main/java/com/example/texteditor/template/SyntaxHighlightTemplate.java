package com.example.texteditor.template;

import com.example.texteditor.Editor;
import com.example.texteditor.ExceptionHandler;
import javax.swing.text.BadLocationException;

public abstract class SyntaxHighlightTemplate {

    protected final Editor editor;

    public SyntaxHighlightTemplate(Editor editor) {
        this.editor = editor;
    }
    public void update() {
        String text;
        try {
            text = editor.textPane.getStyledDocument()
                  .getText(0, editor.textPane.getStyledDocument().getLength());
            highlightSyntax(editor, text);
        } catch (BadLocationException e) {
            new ExceptionHandler().showError("Something gone wrong, please try again");
        }
    }

    public abstract void highlightSyntax(Editor editor, String text);
}
