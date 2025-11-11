package com.raytex.raytex.grammars;


import com.intellij.lexer.FlexAdapter;

public class RaytexLexerAdapter extends FlexAdapter {
    public RaytexLexerAdapter() {
        super(new _RaytexLexer(null));
    }
}
