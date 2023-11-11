package com.example.texteditor.template;

import com.example.texteditor.Editor;
import com.example.texteditor.command.SyntaxHighlightCommand;
import com.example.texteditor.flyweight.SyntaxHighlightAttributesFlyweight;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservedWordSyntaxHighlight extends SyntaxHighlightTemplate {

    public ReservedWordSyntaxHighlight(Editor editor) {
        super(editor);
    }

    @Override
    public void highlightSyntax(Editor editor, String text) {
        Set<String> javaReservedWords = new HashSet<>(Arrays.asList(
              "abstract", "assert", "void", "break", "byte", "case", "catch", "char",
              "class", "const", "continue", "default", "do", "double", "else", "enum",
              "extends", "final", "finally", "float", "for", "goto", "if", "implements",
              "import", "instanceof", "int", "interface", "long", "native", "new", "package",
              "public", "protected", "private", "return", "short", "static", "strictfp",
              "super",
              "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
              "volatile", "while"
        ));
        for (String keyword : javaReservedWords) {
            Pattern pattern = Pattern.compile(keyword + "(\\[\\])*");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor,
                      matcher.start(),
                      keyword.length(),
                      SyntaxHighlightAttributesFlyweight.getKeywordAttributes());
                command.execute();
            }
        }
    }
}
