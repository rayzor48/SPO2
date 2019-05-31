package PARSER;

import Collections.List;
import Collections.Set;
import LEXER.Lexeme;
import LEXER.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    ArrayList<Token> tokens = new ArrayList<>();
    int position = 0;
    boolean go = false;
    Polis polis;
    HashMap<String, Integer> tableOfVar = new HashMap<String, Integer>();
    HashMap<String, List> tableOfCollections = new HashMap<String, List>();
    HashMap<String, Set> tableOfCollectionsSet = new HashMap<String, Set>();

    public Parser(ArrayList<Token> tokens){
        for(Token token : tokens ){
            if(token.getLexeme() != Lexeme.WS){
                this.tokens.add(token);
            }
        }
    }

    public Polis getPolis(){
        if(go){
            polis = new Polis(tokens, tableOfVar);
        }
        return polis;
    }

    public boolean Go(){

        go = lang();
        return go;
    }

    private boolean lang(){
        while (tokens.size() != position){
            if(!expr()){
                return false;
            }
        }
        return true;
    }

    private boolean expr(){
        boolean expr = false;

       System.out.println("Err in : " + position + " name : " + tokens.get(position).getValue() + " type : " + tokens.get(position).getLexeme().toString());

        if(init() || assign() || cust_for() || cust_while() || cust_if() || init_list() || init_set() || functions() ||print()) {
            expr = true;
        }

        return expr;
    }

    private boolean init(){
        int save = position;
        boolean init = false;

        if(getNextLexeme() == Lexeme.INIT){
            tableOfVar.put(tokens.get(position).getValue(), 0);//инициализация переменной
            if(assign()){
                init = true;
            }
        }

        position = init ? position : save;
        return init;
    }

    private boolean checkInit(String key){
        boolean checkInit = false;

        if(tableOfVar.containsKey(key)){
            checkInit = true;
        } else{
            System.err.println("Error: Cannot resolve symbol \"" + key + "\"");
            System.exit(4);
        }

        return checkInit;
    }

    private boolean assign(){
        int save = position;
        boolean assign = false;

        if(getNextLexeme() == Lexeme.VAR){
            if(assign_op()){
                if(getNextLexeme() == Lexeme.END){
                    assign = true;
                }
            }
        }

        position = assign ? position : save;
        return assign;
    }

    private boolean assign_op(){
        int save = position;
        boolean assign_op = false;

        if(getNextLexeme() == Lexeme.ASSIGN_OP) {
            if (value()) {
                assign_op = true;
            }

            if(position < tokens.size()-1) {
                if(tokens.get(position+1).getValue().equals("add")){
                    System.err.println("Error: add does not return value");
                    System.exit(1);
                }
                if (functions()) {
                    position--;
                    assign_op = true;
                }
            }
        }

        position = assign_op ? position : save;
        return assign_op;
    }

    private boolean value(){
        int save = position;
        boolean value = false;

        while (valueOfValue()){}//проматываем до ";"
        position++;

        if(getNextLexeme() == Lexeme.END){value = true;}
        position--;

        position = value ? position : save;
        return value;
    }

    private boolean valueOfValue(){
        int save = position;
        boolean valueOfValue = false;

        if(checkValue()){
            position--;
            if(getNextLexeme() == Lexeme.RP ){//костыль чтобы скобки работали и в конце выражения
                Token t = new Token(getNextLexeme(), "заглушка");
                if(t.getLexeme() == Lexeme.END) {
                    valueOfValue = true;
                    position = position - 3;
                } else if(t.getLexeme() == Lexeme.RP){
                    valueOfValue = true;
                    position = position - 3;
                } else {
                    position = position - 1;
                }
            }

            if(getNextLexeme() == Lexeme.OP){
                valueOfValue = true;
            }
        }

        position = valueOfValue ? position : save;
        return valueOfValue;
    }

    private boolean exprInBrackets(){
        int save = position;
        boolean exprInBrackets = false;

            if (value()) {
                if(getNextLexeme() == Lexeme.RP){
                    exprInBrackets = true;
                }
            }

        position = exprInBrackets ? position : save;
        return exprInBrackets;
    }

    private boolean checkValue(){
        int save = position;
        boolean checkFirstLexeme = false;
        String nameOfVar = tokens.get(position).getValue();

        if (getNextLexeme() == Lexeme.DIGIT){
            checkFirstLexeme = true;
        } else {
            position--;
        }

        if (getNextLexeme() == Lexeme.VAR){
            checkFirstLexeme = checkInit(nameOfVar);
        } else {
            position--;
        }

        if (getNextLexeme() == Lexeme.LP){
            if(exprInBrackets()){
                checkFirstLexeme = true;
            }
        } else {
            position--;
        }

        position = checkFirstLexeme ? position : save;
        return checkFirstLexeme;
    }

    private boolean log_exp(){
        int save = position;
        boolean log_exp = false;

        if (getNextLexeme() == Lexeme.VAR) {
            position --; checkValue();//костыль чтобы в логических выражениях небыло гигантстких арифметических выражений, аля (а * (i-7/p) -2 /(d-3))
            if(getNextLexeme() == Lexeme.LOG_OP){
                if(checkValue()){
                    log_exp = true;
                }
            }
        }

        position = log_exp ? position : save;
        return log_exp;
    }

    private boolean cust_for(){
        int save = position;
        boolean cust_for = false;

        if (getNextLexeme() == Lexeme.FOR) {
            if(for_cond()){
                if(body()){
                    cust_for = true;
                }
            }
        }

        position = cust_for ? position : save;
        return cust_for;
    }

    private boolean for_cond(){// (var peremennaya = 1; peremennaya < 12; peremennaya = peremennaya +1)
        int save = position;
        boolean for_cond = false;

        if (getNextLexeme() == Lexeme.LP) {
            if(init()){
                if(log_exp() && getNextLexeme() == Lexeme.END){
                    if(getNextLexeme() == Lexeme.VAR && assign_op()) {
                        if (getNextLexeme() == Lexeme.RP) {
                            for_cond = true;
                        }
                    }
                }
            }
        }

        position = for_cond ? position : save;
        return for_cond;
    }

    private boolean body(){
        int save = position;
        boolean for_body = true;

        if (getNextLexeme() == Lexeme.LP_F) {
            while (getNextLexeme() != Lexeme.RP_F) {
                position--;
                if (!expr()) {
                    for_body = false;
                }
            }
        }

        position = for_body ? position : save;
        return for_body;
    }

    private boolean cust_while(){
        int save = position;
        boolean cust_while = false;

        if (getNextLexeme() == Lexeme.WHILE) {
            if(while_cond()){
                if(body()){
                    cust_while = true;
                }
            }
        }

        position = cust_while ? position : save;
        return cust_while;
    }

    private boolean while_cond(){
        int save = position;
        boolean while_cond = false;

        if (getNextLexeme() == Lexeme.LP) {
            if (log_exp()) {
                if (getNextLexeme() == Lexeme.RP) {
                    while_cond = true;
                }
            }
        }

        position = while_cond ? position : save;
        return while_cond;
    }

    private boolean cust_if(){
        int save = position;
        boolean cust_if = false;

        if (getNextLexeme() == Lexeme.IF) {
            if(while_cond()){
                if(body()){
                    cust_if = true;
                }
            }
        }

        position = cust_if ? position : save;
        return cust_if;
    }

    private boolean checkInitList(){
        boolean checkInitList = false;

        if(tableOfCollections.containsKey(tokens.get(position++).getValue())){
            checkInitList = true;
        }
        if(!checkInitList){
            position --;
            if(tableOfCollectionsSet.containsKey(tokens.get(position++).getValue())){
                checkInitList = true;
            }
        }

        return checkInitList;
    }

    private boolean init_list(){
        int save = position;
        boolean init_list = false;

        if(getNextLexeme() == Lexeme.INIT_LIST){
            tableOfCollections.put(tokens.get(position).getValue(), new List<String>());
            if(getNextLexeme() == Lexeme.LIST){
                if(getNextLexeme() == Lexeme.END) {
                    init_list = true;
                }
            }
        }

        position = init_list ? position : save;
        return init_list;
    }

    private boolean init_set(){
        int save = position;
        boolean init_set = false;

        if(getNextLexeme() == Lexeme.INIT_SET){
            tableOfCollectionsSet.put(tokens.get(position).getValue(), new Set<String>());
            if(getNextLexeme() == Lexeme.LIST){
                if(getNextLexeme() == Lexeme.END) {
                    init_set = true;
                }
            }
        }

        position = init_set ? position : save;
        return init_set;
    }

    private boolean functions() {
        int save = position;
        boolean functions = false;
        String check = "";
        check = tokens.get(position).getValue();
        if (checkInitList()) {
            if (getNextLexeme() == Lexeme.FUNCTIONS) {
                position--;
                if(tableOfCollectionsSet.containsKey(check)) {
                    check = tokens.get(position).getValue();
                    if(check.equals("get")){
                        System.err.println("Error: Set does not have a get function");
                        System.exit(404);
                    }
                }
                if (tokens.get(position++).getValue().equals("size")) {
                    functions = true;
                } else {
                    if (tokens.get(position).getLexeme() == Lexeme.VAR) {
                        checkInit(tokens.get(position).getValue());
                        functions = true;
                    } else if (tokens.get(position).getLexeme() == Lexeme.DIGIT) {
                        functions = true;
                    }
                    position++;
                }
            }
        }

        if(getNextLexeme() != Lexeme.END){
            functions = false;
        }

        position = functions ? position : save;
        return functions;
    }

    private boolean print(){
        int save = position;
        boolean print = false;

        if(getNextLexeme() == Lexeme.PRINT){
            if(getNextLexeme() == Lexeme.END){
                print = true;
            }
        }

        position = print ? position : save;
        return print;
    }

    public HashMap<String, List> getTableOfCollections() {
        return tableOfCollections;
    }

    public HashMap<String, Set> getTableOfCollectionsSet() {
        return tableOfCollectionsSet;
    }

    private Lexeme getNextLexeme(){//возвращает следующий токен
        try {
            return tokens.get(position++).getLexeme();
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("Error: Lexeme \"" + "\" expected");
            System.exit(1);
        }
        return null;
    }
}
