package com.example.texteditor;

import com.example.texteditor.observer.SyntaxHighlight;

public class TextEditor {

    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.init();
        new SyntaxHighlight(editor);
    }

}
