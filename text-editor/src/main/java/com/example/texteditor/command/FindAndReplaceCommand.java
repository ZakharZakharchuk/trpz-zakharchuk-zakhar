package com.example.texteditor.command;

import com.example.texteditor.Editor;

public class FindAndReplaceCommand extends Command {

    private final String searchText;
    private final String replaceText;

    public FindAndReplaceCommand(Editor editor, String searchText, String replaceText) {
        super(editor);
        this.searchText = searchText;
        this.replaceText = replaceText;
    }

    @Override
    public boolean execute() {
        backup();
        String text = editor.textPane.getText();
        text = text.replace(searchText, replaceText);
        editor.textPane.setText(text);
        return true;
    }
}
