lexer grammar LITTLE;

KEYWORD : 'PROGRAM'|'BEGIN'|'END'|'FUNCTION'|'READ'|'WRITE'|'IF'|'ELSE'|'FI'|'FOR'|'ROF'|'RETURN'|'INT'|'VOID'|'STRING'|'FLOAT'|'WHILE'|'ENDIF'|'ENDWHILE';

IDENTIFIER : [a-zA-Z]+[a-zA-Z0-9]*;

INTLITERAL : [0-9]+;

FLOATLITERAL : [0-9]*[.]+[0-9]+;

STRINGLITERAL : '"'(.*?)'"';

COMMENT : '--'[^\n]*;

OPERATOR : ':='|'+'|'-'|'*'|'/'|'='|'!='|'<'|'>'|'('|')'|';'|','|'<='|'>=';

WS : [ \t\r\n]+ -> skip ;

