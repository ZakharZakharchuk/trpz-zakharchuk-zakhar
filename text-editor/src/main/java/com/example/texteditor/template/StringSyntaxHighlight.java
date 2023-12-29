package com.example.texteditor.template;

import com.example.texteditor.Editor;
import com.example.texteditor.command.SyntaxHighlightCommand;
import com.example.texteditor.flyweight.SyntaxHighlightAttributesFlyweight;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSyntaxHighlight extends SyntaxHighlightTemplate{

    public StringSyntaxHighlight(Editor editor) {
        super(editor);
    }

    @Override
    public void highlightSyntax(Editor editor, String text) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor, matcher.start(),
                  matcher.end() - matcher.start(),
                  SyntaxHighlightAttributesFlyweight.getStringAttributes());
            command.execute();
        }
    }
}
