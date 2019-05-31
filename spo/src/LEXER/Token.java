package LEXER;

public class Token {
    private Lexeme lexeme;
    private String value;

    public Token (Lexeme l, String s){
        lexeme = l;
        value = s;
    }

    public Lexeme getLexeme(){
        return lexeme;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        return "[ Lexeme : \"" + lexeme + "\" Value : \"" + value + "\"]";
    }
}
