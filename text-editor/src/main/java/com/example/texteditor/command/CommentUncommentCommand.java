package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.service.MacroService;

public class CommentUncommentCommand extends Command {

    public CommentUncommentCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        backup();
        new MacroService().commentUncommentBlock(editor.textPane);
        return true;
    }

}
