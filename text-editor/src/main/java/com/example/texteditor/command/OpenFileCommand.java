package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.service.FileService;

public class OpenFileCommand extends Command {

    public OpenFileCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        new FileService(editor).openFile();
        return false;
    }
}
