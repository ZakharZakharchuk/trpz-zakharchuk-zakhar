package com.example.texteditor.strategy;

import java.io.File;

public interface EncodingStrategy {
    public String encode(byte[] textBytes);
}
