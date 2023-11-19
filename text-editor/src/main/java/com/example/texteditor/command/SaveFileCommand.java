package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.service.FileService;

public class SaveFileCommand extends Command {

    public SaveFileCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        new FileService(editor).saveFile();
        return false;
    }
}
