package com.example.texteditor.strategy;

import java.nio.charset.StandardCharsets;

public class ISOStrategy implements EncodingStrategy{
    @Override
    public String encode(byte[] textBytes) {
        return new String(textBytes, StandardCharsets.ISO_8859_1);
    }
}
