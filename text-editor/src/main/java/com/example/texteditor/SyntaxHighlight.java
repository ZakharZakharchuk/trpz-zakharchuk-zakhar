package com.example.texteditor;

import com.example.texteditor.command.SyntaxHighlightCommand;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class SyntaxHighlight {
    public void getAttributesForText(Editor editor, StyledDocument document, int start,
          int length) {
        String text;
        try {
            text = document.getText(start, length);
            System.out.println(text);
            isReservedWord(editor, text);
            isString(editor, text);
            isNumber(editor, text);
            isAnnotation(editor, text);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }
    private void isReservedWord(Editor editor, String text) {
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

    private void isString(Editor editor, String text) {
        Pattern pattern = Pattern.compile("\"[^\"]*\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor, matcher.start(),
                  matcher.end() - matcher.start(),
                  SyntaxHighlightAttributesFlyweight.getStringAttributes());
            command.execute();
        }
    }

    private void isNumber(Editor editor, String text) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor, matcher.start(),
                  matcher.end() - matcher.start(),
                  SyntaxHighlightAttributesFlyweight.getNumberAttributes());
            command.execute();
        }
    }

    private void isAnnotation(Editor editor, String text) {
        Pattern pattern = Pattern.compile("^@.*");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            SyntaxHighlightCommand command = new SyntaxHighlightCommand(editor, matcher.start(),
                  matcher.end() - matcher.start(),
                  SyntaxHighlightAttributesFlyweight.getAnnotationAttributes());
            command.execute();
        }
    }
}
