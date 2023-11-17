package com.example.texteditor.command;

import com.example.texteditor.service.BookmarkService;
import com.example.texteditor.Editor;

public class RemoveBookmarkCommand extends Command {

    public RemoveBookmarkCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        new BookmarkService().removeBookmark(editor);
        return false;
    }

}
