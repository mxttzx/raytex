package com.raytex.raytex.grammars;

import com.intellij.psi.tree.IElementType;
import static com.raytex.raytex.grammars.RaytexTokenTypes.*;

%%

%class _RaytexLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE = [ \t\n\r]+
LATEX_DELIM = \$\$
TEXT = [^$]+

%%

{WHITE_SPACE}         { return com.intellij.psi.TokenType.WHITE_SPACE; }
{LATEX_DELIM}         { return LATEX_DELIM; }
{TEXT}                { return COMMENT_TEXT; }

<<EOF>>               { return null; }
