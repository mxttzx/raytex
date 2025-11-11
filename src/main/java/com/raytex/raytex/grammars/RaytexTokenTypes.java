package com.raytex.raytex.grammars;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public interface RaytexTokenTypes {
    String delim = "LATEX_INLINE";
    String comment = "COMMENT_TEXT";

    IElementType LATEX_DELIM = new RaytexTokenType(delim);
    IElementType COMMENT_TEXT = new RaytexTokenType(comment);
}
