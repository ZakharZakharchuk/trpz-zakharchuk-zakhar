package com.example.texteditor.strategy;

import java.nio.charset.Charset;

public class CP866Strategy implements EncodingStrategy{

    @Override
    public String encode(byte[] textBytes) {
        Charset cp866Charset = Charset.forName("CP866");
        return new String(textBytes, cp866Charset);
    }
}
