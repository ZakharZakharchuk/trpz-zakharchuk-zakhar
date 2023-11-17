package com.example.texteditor.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Snippet {
    private String name;
    private String code;
}
