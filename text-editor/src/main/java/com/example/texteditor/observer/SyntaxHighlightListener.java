package com.example.texteditor.observer;

import com.example.texteditor.Editor;
import com.example.texteditor.template.AnnotationSyntaxHighlight;
import com.example.texteditor.template.NumberSyntaxHighlight;
import com.example.texteditor.template.ReservedWordSyntaxHighlight;
import com.example.texteditor.template.StringSyntaxHighlight;
import com.example.texteditor.template.SyntaxHighlightTemplate;

public class SyntaxHighlightListener implements Observer {

    private final SyntaxHighlightTemplate reservedWordHighlight;
    private final SyntaxHighlightTemplate stringHighlight;
    private final SyntaxHighlightTemplate numberHighlight;
    private final SyntaxHighlightTemplate annotationHighlight;

    public SyntaxHighlightListener(Editor editor, ObserverManager observerManager) {
        reservedWordHighlight = new ReservedWordSyntaxHighlight(editor);
        stringHighlight = new StringSyntaxHighlight(editor);
        numberHighlight = new NumberSyntaxHighlight(editor);
        annotationHighlight = new AnnotationSyntaxHighlight(editor);
        observerManager.addObserver(this);
    }

    @Override
    public void update() {
        reservedWordHighlight.update();
        stringHighlight.update();
        numberHighlight.update();
        annotationHighlight.update();
    }
}
