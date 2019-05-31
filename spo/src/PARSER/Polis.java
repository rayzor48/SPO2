package PARSER;

import LEXER.Lexeme;
import LEXER.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Polis {
    private ArrayList<Token> tokens;
    private ArrayList<Token> polis = new ArrayList<Token>();
    private int position = 0, start;
    private Stack<Token> buffer = new Stack<>();
    private Stack<Integer> buffer_starts  = new Stack<>();

    HashMap<String, Integer> tableOfVar;
    public Polis (ArrayList<Token> tokens, HashMap<String, Integer> tableOfVar){
        this.tokens = tokens;
        this.tableOfVar = tableOfVar;

        createPolis();
    }

    private void createPolis(){
        Token t = null;

        while (position < tokens.size()){
            if(getToken().getLexeme() == Lexeme.VAR){
                polis.add(getToken());
            } else if(getToken().getLexeme() == Lexeme.DIGIT) {
                polis.add(getToken());
            } else if(getToken().getLexeme() == Lexeme.LIST) {
                position ++;
                if(tokens.get(position--).getLexeme() != Lexeme.END){
                    polis.add(getToken());
                }
            } else if(getToken().getLexeme() == Lexeme.OP) {

                if(buffer.peek().getLexeme() == Lexeme.ASSIGN_OP || buffer.peek().getLexeme() == Lexeme.LP) {
                    buffer.push(getToken());
                } else if( getPriority(getToken()) > getPriority(buffer.peek())){
                    buffer.push(getToken());
                } else {
                    while (buffer.size() > 1 && buffer.peek().getLexeme() != Lexeme.LP) {
                        if(getPriority(getToken()) <= getPriority(buffer.peek())) {
                            polis.add(buffer.pop());
                        } else break;
                    }
                    buffer.push(getToken());
                }

            }  else if(getToken().getLexeme() == Lexeme.FUNCTIONS) {

                buffer.push(getToken());

            } else if(getToken().getLexeme() == Lexeme.ASSIGN_OP) {
                buffer.add(getToken());
            } else if(getToken().getLexeme() == Lexeme.END) {
                while (!buffer.empty() && buffer.peek().getLexeme() != Lexeme.LP){
                    polis.add(buffer.pop());
                }
            } else if(getToken().getLexeme() == Lexeme.LP) {
                buffer.push(getToken());
            } else if(getToken().getLexeme() == Lexeme.RP) {
                while (buffer.peek().getLexeme() != Lexeme.LP) {
                        polis.add(buffer.pop());
                }
                buffer.pop();
            } else if(getToken().getLexeme() == Lexeme.LOG_OP) {
                buffer.push(getToken());
                start = polis.size() - 1;
                buffer_starts.push(start);
            } else if(getToken().getLexeme() == Lexeme.LP_F) {
                polis.add(start + 3, t);
                polis.add(start + 4, new Token(Lexeme.NF, "!F"));
            } else if(getToken().getLexeme() == Lexeme.RP_F) {
                start = buffer_starts.pop();
                if( polis.get(start + 3).getValue().equals("if")) {
                    polis.get(start + 3).setValue(String.valueOf(polis.size()));
                } else {
                    polis.get(start + 3).setValue(String.valueOf(polis.size() + 2));
                    polis.add(new Token(Lexeme.DIGIT, String.valueOf(start)));
                    polis.add(new Token(Lexeme.F, "!"));
                }
            } else if(getToken().getLexeme() == Lexeme.WHILE || getToken().getLexeme() == Lexeme.FOR){
                t = new Token(Lexeme.DIGIT, "cicle");
            } else if(getToken().getLexeme() == Lexeme.IF){
                t = new Token(Lexeme.DIGIT, "if");
            } else if(getToken().getLexeme() == Lexeme.PRINT){
                polis.add(new Token(Lexeme.PRINT, "print"));
            }
            position++;
        }

        //System.out.println(polis.toString());
    }

    private int getPriority(Token token){//получение приоритета операции (+|-) - приоритет 1, иначе (*|/) - приоритет 2
        int priority = 0;

        if(token.getValue().equals("-") || token.getValue().equals("+")){
            priority = 1;
        } else {
            priority = 2;
        }

        return priority;
    }

    private Token getToken(){
        return tokens.get(position);
    }

    public HashMap<String, Integer> getTableOfVar() {
        return tableOfVar;
    }

    public ArrayList<Token> getPolis(){
        polis.add(new Token (Lexeme.EOF, "EOF"));
        return polis;
    }

}
