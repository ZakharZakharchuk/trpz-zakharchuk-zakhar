package com.example.texteditor.strategy;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class UTFStrategy implements EncodingStrategy {

    @Override
    public String encode(byte[] textBytes) {
        return new String(textBytes, StandardCharsets.UTF_8);
    }
}
