package LEXER;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class Lexer {

    private String input;//отсюда будем читать код на "твоем" языке
    private String acc = "";
    private Lexeme lexeme;
    private int pos = 0;

    public Lexer(String s){//конструктор
        input = s;
    }

    public ArrayList<Token> getTokens(){
        ArrayList<Token> tokens = new ArrayList<>();

        while (pos < input.length()){//идем по строке пока позиция меньше ее длины
            acc = acc + input.charAt(pos);//добавляем в асс по 1 символу из input
            if(!check(acc)){//если асс подходит под лексему, то увеличиваем позицию, иначе:
                if(check(String.valueOf(acc.charAt(acc.length() - 1)))){// проверяем последний символ, если он не соответствует списку лексем - выплевываем еррор
                    if(check(acc = acc.substring(0, acc.length() - 1))){// если последний символ подошел, то проверяем все строку предыдущих, чтобы выбралась соответствующая lexemе
                        tokens.add(new Token(lexeme, acc));//добавляем новый токен в список токенов
                        acc = "";//обнуляем временную строку
                    }
                } else {//выплевываем еррор
                    System.err.println('\n' + "Can't recognize symbol '" + input.charAt(pos) + "' at position:" + pos + "!");
                    System.exit(1);
                }
            } else {
                pos++;
            }
        }

        tokens.add(new Token(lexeme, acc));//добавляем последний токен тут, потому что pos = lenght и цикл завершился

        return tokens;//возвращаем список токенов
    }

    private boolean check(String s) {//проверяем строку на соответствие нашим лексемам
        for (Lexeme l : Lexeme.values()) {
            Matcher matcher = l.getPattern().matcher(s);
            if (matcher.matches()) {
                lexeme = l;
                return true;
            }
        }

        return false;
    }

}
