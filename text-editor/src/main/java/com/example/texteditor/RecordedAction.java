package com.example.texteditor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecordedAction {
    private ActionType type;
    private String actionCommand; // For ActionEvent
    private String typedText; // For text edits
    private int keyCode;
}
