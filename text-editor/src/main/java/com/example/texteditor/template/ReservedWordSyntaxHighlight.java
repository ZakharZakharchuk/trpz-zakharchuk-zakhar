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

    private static final Set<String> javaReservedWords = new HashSet<>(Arrays.asList(
          "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
          "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
          "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
          "interface", "long", "native", "new", "package", "private", "protected", "public",
          "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
          "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false",
          "null"));

    public ReservedWordSyntaxHighlight(Editor editor) {
        super(editor);
    }

    @Override
    public void highlightSyntax(Editor editor, String text) {
        javaReservedWords.forEach(keyword -> {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor,
                      matcher.start(),
                      keyword.length(),
                      SyntaxHighlightAttributesFlyweight.getKeywordAttributes());
                command.execute();
            }
        });
    }
}
