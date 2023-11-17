package com.example.texteditor;

import com.example.texteditor.observer.ObserverManager;
import com.example.texteditor.observer.SyntaxHighlight;

public class TextEditor {

    public static void main(String[] args) {
        ObserverManager observerManager = new ObserverManager();
        Editor editor = new Editor(observerManager);
        editor.init();
        new SyntaxHighlight(editor,observerManager);
    }

}
