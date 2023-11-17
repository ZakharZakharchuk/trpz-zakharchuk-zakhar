package com.example.texteditor.strategy;

public interface EncodingStrategy {
    String encode(byte[] textBytes);
}
