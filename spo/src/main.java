import LEXER.Lexer;
import LEXER.Token;
import PARSER.Interpretator;
import PARSER.Parser;

import java.util.ArrayList;

public class main {

    public static void main(String []args) {
        ArrayList<Token> tokens = new ArrayList<>();
        Lexer lexer = new Lexer("var a = 0; set @set; @set add 1;@set add 2; @set add a; @set add 4; @set remove 1;  @set add 1; set @setdva; @setdva add 32;@setdva add 3424; a = @set size; print;");//сюда вводишь строку которую хочешь разбить на токены
        //a = (a + b) * c/d + (g - (j+ 9))   (a + b) * (c/d + (g - j))+ 9; // var a = 11; var b = 22; var f = 33;if(a < b){a = f;} a = 44; // var a = 0; for (var i = 0;i < 5; i = i + 1) {for(var j = 5; j > i; j = j - 1){a = i + (j *10);}} print;
        tokens = lexer.getTokens();

        for (Token token : tokens) {//вывод списка токенов
            System.out.println(token);
        }

        Parser parser = new Parser(tokens);
        if (parser.Go()) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }


        System.out.println("Poliz : ");
        for (Token token : parser.getPolis().getPolis()) {
            System.out.print(" " + token.getValue() + ", ");
        }

        Interpretator b = new Interpretator(parser);
    }
}
