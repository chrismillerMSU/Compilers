import java.util.HashMap;
import java.util.Stack;

public class MyListener extends LITTLEBaseListener{
    private Stack symbolTableStack;
    private Stack scopeStack;
    private String currentType;
    private String type;
    private HashMap values;
    private int blockNumber;

    MyListener(){
        symbolTableStack = new Stack<HashMap>();
        scopeStack = new Stack<String>();
        blockNumber = 1;
        type = "";
    }




    @Override public void enterProgram(LITTLEParser.ProgramContext ctx) {
        scopeStack.push("GLOBAL");
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
        System.out.println("END");
        System.out.println(symbolTableStack);
    }

    @Override public void enterFunc_declarations(LITTLEParser.Func_declarationsContext ctx) {
        if(scopeStack.contains("GLOBAL")){
            System.out.println(scopeStack.pop());
            System.out.println(symbolTableStack.peek());
        }
    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        type = "funct";
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
        System.out.println(scopeStack.pop());
        System.out.println(symbolTableStack.pop());
    }

    @Override public void enterVar_type(LITTLEParser.Var_typeContext ctx) {
        currentType = ctx.getText();
    }

    @Override public void enterString_decl(LITTLEParser.String_declContext ctx) {
        currentType = "STRING";
        type = "str";
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
            symbolTable.putIfAbsent(name, currentType);
        }else if(type.equals("funct")){
            scopeStack.push(name);
        }
    }
}
