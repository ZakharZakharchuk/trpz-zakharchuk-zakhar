package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.service.MacroService;

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
        new MacroService().findAndReplace(editor.textPane, searchText, replaceText);
        return true;
    }
}
