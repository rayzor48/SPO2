package PARSER;

import LEXER.Lexeme;
import LEXER.Token;
import Collections.List;
import Collections.Set;

import java.util.*;

public class Interpretator {
    HashMap <String, Integer> tableOfVar;
    ArrayList<Token> polis;
    private Stack<String> buffer = new Stack<>();
    private HashMap<String, List> tableOfCollections;
    private HashMap<String, Set> tableOfCollectionsSet;
    List<String> list = null;
    Set<String> set = null;

    int a, b, c;
    boolean flag = false;

    public Interpretator(Parser parser){
        tableOfVar = parser.getPolis().getTableOfVar();
        tableOfCollections = parser.getTableOfCollections();
        tableOfCollectionsSet = parser.getTableOfCollectionsSet();
        polis = parser.getPolis().getPolis();

        Start();
    }

    private void Start(){
        Token tok;
        for(int position = 0; position <= polis.size() - 1; position ++ ){
            tok = polis.get(position);
            //System.out.println( " my Pos = " + position + " - " +  tok.getValue());

            if(tok.getLexeme() == Lexeme.VAR){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.DIGIT){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.LIST){
                buffer.push(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.OP){
                operation(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.FUNCTIONS){
                functions(tok.getValue());
            } else if (tok.getLexeme() == Lexeme.LOG_OP){
                buffer.push(String.valueOf(logicOperation(tok.getValue())));
            } else if (tok.getLexeme() == Lexeme.ASSIGN_OP){
                assignOp();
            } else if (tok.getLexeme() == Lexeme.NF){
                a = valueOrVariable(tableOfVar) -1;
                flag = buffer.pop().equals("true");
                position = flag ? position : a;
            } else if (tok.getLexeme() == Lexeme.F){
                a = valueOrVariable(tableOfVar) - 1;
                    position  = a;
            }else if (tok.getLexeme() == Lexeme.PRINT){
                System.out.println("\nVariables : " +  tableOfVar + "\n");
                System.out.println("Lists: " + tableOfCollections);
                System.out.println("Set: " + tableOfCollectionsSet);
            } else if (tok.getLexeme() == Lexeme.EOF){
                //System.out.println("\n\nEOF!!! ");
            }

            //System.out.println(tableOfVar.toString());
        }
    }

    private void operation(String op) {
        setAB();
        switch (op) {
            case "+":
                c = a + b;
                break;
            case "-":
                c = a - b;
                break;
            case "/":
                c = a / b;
                break;
            case "*":
                c = a * b;
                break;
        }

        buffer.push(String.valueOf(c));
    }

    private boolean logicOperation(String logOp){
        boolean flag = false;
        setAB();
        switch (logOp) {
            case "<":
                flag = a < b;
                break;
            case ">":
                flag = a > b;
                break;
            case "==":
                flag = a == b;
                break;
            case "!=":
                flag = a != b;
                break;
            case "<=":
                flag = a <= b;
                break;
            case ">=":
                flag = a >= b;
                break;
        }

        return flag;
    }

    private boolean functions(String funktions){
        if(!funktions.equals("size")){
            a = valueOrVariable(tableOfVar);//System.out.print("a = " + a);
        }

        boolean flagContains = false;

        String s = "";
        if(tableOfCollections.containsKey(buffer.peek())){
            list = tableOfCollections.get(buffer.pop());
            flagContains = true;
        } else if(tableOfCollectionsSet.containsKey(buffer.peek())){
            set = tableOfCollectionsSet.get(buffer.pop());
            flagContains = true;
        }

        if(flagContains) {
            switch (funktions) {
                case "add":
                    if(list != null){
                        list.add(String.valueOf(a));
                    } else if(set != null) {
                        set.add(String.valueOf(a));
                    }
                    break;
                case "get":
                    if(list != null){
                        s = list.get(a);
                    } else if(set != null) {
                        //ошибка!!!
                        //еррор
                    }
                    break;
                case "remove":
                    if(list != null){
                        s = list.remove(a);
                    } else if(set != null) {
                        if(set.remove(String.valueOf(a))){
                            s = "1";
                        } else {
                            s = "0";
                        }
                    }
                    break;
                case "size":
                    if(list != null){
                        s = String.valueOf(list.getSize());
                    } else if(set != null) {
                        s = String.valueOf(set.getSize());
                    }
                    break;
            }
        }

        if(!s.equals("")){
            buffer.push(String.valueOf(s));
        }

        set = null;
        list = null;

        return flag;
    }

    private void assignOp(){
        a = valueOrVariable(tableOfVar);
        tableOfVar.put(buffer.pop(), a);
    }

    private int valueOrVariable(Map<String, Integer> table) throws EmptyStackException {
        if(isDigit(buffer.peek())){
            return Integer.valueOf(buffer.pop());
        } else if(!isDigit(buffer.peek())){
            return table.get(buffer.pop());
        } else{
            System.err.println();
            System.exit(10);
        }

        return -1;
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setAB(){
        b = valueOrVariable(tableOfVar);
        a = valueOrVariable(tableOfVar);
    }
}