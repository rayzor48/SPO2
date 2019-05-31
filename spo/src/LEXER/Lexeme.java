package LEXER;

import java.util.regex.Pattern;

public enum Lexeme {
        INIT(Pattern.compile("^var$")),
        INIT_LIST(Pattern.compile("^list$")),
        INIT_SET(Pattern.compile("^set")),
        FOR(Pattern.compile("^for")),
        WHILE(Pattern.compile("^while")),
        IF(Pattern.compile("^if$")),
        EOF(Pattern.compile("^EOF")),
        PRINT(Pattern.compile("^print$")),
        FUNCTIONS(Pattern.compile("^add|get|remove|size$")),

        NF(Pattern.compile("^!F$")),
        F(Pattern.compile("^!$")),

        LOG_OP(Pattern.compile("^<|>|<=|>=|!=|==$")),

        DIGIT(Pattern.compile("^0|[1-9][0-9]*$")),
        VAR(Pattern.compile("^[a-z]+$")),
        LIST(Pattern.compile("^@[a-z]*$")),//теперь это токен определяет название коллекций, а не листа!!!
        ASSIGN_OP(Pattern.compile("^=$")),
        OP(Pattern.compile("^\\+|\\-|\\*|\\/$")),
        WS(Pattern.compile("^\\s+")),

        LP_F(Pattern.compile("^\\{$")),
        RP_F(Pattern.compile("^}$")),
        LP(Pattern.compile("^\\($")),
        RP(Pattern.compile("^\\)$")),

        END(Pattern.compile("^;$"));

        private Pattern pattern;

        Lexeme(Pattern pattern){
            this.pattern = pattern;
        }

        public Pattern getPattern(){
            return pattern;
        }

}
