package com.example.texteditor.command;

import com.example.texteditor.service.BookmarkService;
import com.example.texteditor.Editor;

public class SetBookmarkCommand extends Command {

    public SetBookmarkCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        new BookmarkService().setBookmark(editor);
        return false;
    }

}
