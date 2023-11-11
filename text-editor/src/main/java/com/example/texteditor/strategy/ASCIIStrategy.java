package com.example.texteditor.strategy;

import java.nio.charset.StandardCharsets;

public class ASCIIStrategy implements EncodingStrategy{
    @Override
    public String encode(byte[] textBytes) {
        return new String(textBytes, StandardCharsets.US_ASCII);
    }
}
