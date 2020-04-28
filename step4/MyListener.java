import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MyListener extends LITTLEBaseListener{
    private Stack symbolTableStack;
    private Queue<String> scopeOrder;
    private Queue<String> tokenOrder;
    private String currentType;
    private String type;
    private HashMap values;
    private String lastName;
    private int blockNumber;
    private boolean ifOrWhile;

    MyListener(){
        symbolTableStack = new Stack<HashMap>();
        scopeOrder = new LinkedList<>();
        tokenOrder = new LinkedList<>();
        values = new HashMap<String, String>();
        blockNumber = 1;
        type = "";
        lastName = "";
        ifOrWhile = false;
    }

    public HashMap<String, String> getSymbolTable(){
        return (HashMap<String, String>) symbolTableStack.peek();
    }

    private void printTable(HashMap symbolTable){
        System.out.println("Symbol table "+scopeOrder.remove());
        System.out.println(symbolTable);
        while (tokenOrder.size()>0){
            String name = tokenOrder.remove();
            String type = (String) symbolTable.get(name);
            System.out.print("name " + name + " type " + type);
            if(type.equals("STRING")){
                System.out.println(" value " + values.get(name));
            }else{
                System.out.println();
            }
        }
        System.out.println();
    }


    @Override public void enterProgram(LITTLEParser.ProgramContext ctx) {
        scopeOrder.add("GLOBAL");
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
//        System.out.println();
    }

    @Override public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
//        if(!ifOrWhile) {
//            printTable((HashMap) symbolTableStack.peek());
//        }
        ifOrWhile = true;
        scopeOrder.add("BLOCK " + blockNumber);
        symbolTableStack.push(new HashMap<String, String>());
        blockNumber++;
    }

    @Override public void enterElse_part(LITTLEParser.Else_partContext ctx) {
        if(!ctx.getText().equals("")) {
            symbolTableStack.pop(); //Was printTable
            scopeOrder.add("BLOCK " + blockNumber);
            symbolTableStack.push(new HashMap<String, String>());
            blockNumber++;
        }
    }
    @Override public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
        symbolTableStack.pop(); //Was printTable
    }

    @Override public void enterWhile_stmt(LITTLEParser.While_stmtContext ctx) {
//        if(!ifOrWhile) {
//            printTable((HashMap) symbolTableStack.peek());
//        }
        ifOrWhile = true;
        scopeOrder.add("BLOCK " + blockNumber);
        symbolTableStack.push(new HashMap<String, String>());
        blockNumber++;
    }

    @Override public void exitWhile_stmt(LITTLEParser.While_stmtContext ctx) {
        symbolTableStack.pop(); //Was printTable
    }

    @Override public void enterFunc_declarations(LITTLEParser.Func_declarationsContext ctx) {
//        if(scopeOrder.contains("GLOBAL")){
//            printTable((HashMap) symbolTableStack.peek());
//        }

    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        type = "funct";
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
        if(ifOrWhile) {
            ifOrWhile = false;
        }else{
            symbolTableStack.pop(); //Was printTable
        }
    }

    @Override public void enterVar_type(LITTLEParser.Var_typeContext ctx) {
        currentType = ctx.getText();
    }

    @Override public void enterString_decl(LITTLEParser.String_declContext ctx) {
        currentType = "STRING";
        type = "str";
    }
    @Override public void enterStr(LITTLEParser.StrContext ctx) {
        values.put(lastName, ctx.getText());
    }

    @Override public void exitString_decl(LITTLEParser.String_declContext ctx) {
        type = "";
    }

    @Override public void enterParam_decl_list(LITTLEParser.Param_decl_listContext ctx){
        type = "varDec";
    }

    @Override public void exitParam_decl_list(LITTLEParser.Param_decl_listContext ctx){
        type = "";
    }

    @Override public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
        type = "varDec";
    }

    @Override public void exitVar_decl(LITTLEParser.Var_declContext ctx) {
        type = "";
    }

    @Override public void enterId(LITTLEParser.IdContext ctx) {
        String name = ctx.getText();
        if(type.equals("varDec") || type.equals("str")) {
            HashMap symbolTable = (HashMap) symbolTableStack.peek();
            if(symbolTable.containsKey(name)){
                throw new Error("DECLARATION ERROR " + name);
            }
            symbolTable.put(name, currentType);
            tokenOrder.add(name);
            lastName = name;
        }else if(type.equals("funct")){
            scopeOrder.add(name);
        }
    }
}
