package com.raytex.raytex.grammars;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class RaytexTokenType extends IElementType {
    public RaytexTokenType(@NotNull @NonNls String debugName) {
        super(debugName, Language.ANY);
    }

    @Override
    public String toString() {
        return "RaytexTokenType." + super.toString();
    }
}
