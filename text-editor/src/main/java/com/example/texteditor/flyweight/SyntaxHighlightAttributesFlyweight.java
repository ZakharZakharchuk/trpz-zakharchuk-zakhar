package com.example.texteditor.flyweight;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SyntaxHighlightAttributesFlyweight {

    private static final SimpleAttributeSet keywordAttributes = new SimpleAttributeSet();
    private static final SimpleAttributeSet stringAttributes = new SimpleAttributeSet();
    private static final SimpleAttributeSet numberAttributes = new SimpleAttributeSet();
    private static final SimpleAttributeSet annotationAttributes = new SimpleAttributeSet();

    static {
        StyleConstants.setForeground(keywordAttributes, Color.BLACK);
        StyleConstants.setBold(keywordAttributes, true);

        StyleConstants.setForeground(stringAttributes, Color.GREEN);

        StyleConstants.setForeground(numberAttributes, Color.CYAN);

        StyleConstants.setForeground(annotationAttributes, Color.ORANGE);
    }

    public static SimpleAttributeSet getKeywordAttributes() {
        return keywordAttributes;
    }

    public static SimpleAttributeSet getStringAttributes() {
        return stringAttributes;
    }

    public static SimpleAttributeSet getNumberAttributes() {
        return numberAttributes;
    }

    public static SimpleAttributeSet getAnnotationAttributes() {
        return annotationAttributes;
    }
}
