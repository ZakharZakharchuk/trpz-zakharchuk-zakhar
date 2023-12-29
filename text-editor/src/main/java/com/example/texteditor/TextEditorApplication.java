package com.example.texteditor;

import javax.swing.SwingUtilities;

public class TextEditorApplication {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Editor::new);
    }

}
