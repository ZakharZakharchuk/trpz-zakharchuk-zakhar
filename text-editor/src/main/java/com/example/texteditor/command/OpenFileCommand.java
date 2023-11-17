package com.example.texteditor.command;

import com.example.texteditor.Editor;
import com.example.texteditor.strategy.ASCIIStrategy;
import com.example.texteditor.strategy.EncodingStrategy;
import com.example.texteditor.strategy.ISOStrategy;
import com.example.texteditor.strategy.UTFStrategy;

public class OpenFileCommand {

    private final Editor editor;

    public OpenFileCommand(Editor editor) {
        this.editor = editor;
    }

    public boolean execute(byte[] fileBytes, String encoding) {
        EncodingStrategy encodingStrategy = switch (encoding) {
            case "UTF8" -> new UTFStrategy();
            case "US-ASCII" -> new ASCIIStrategy();
            case "ISO-8859-1" -> new ISOStrategy();
            default -> throw new IllegalArgumentException("Unsupported encoding");
        };
        String text = encodingStrategy.encode(fileBytes);
        editor.textPane.setText(text);
        return false;
    }
}
