import java.util.HashMap;
import java.util.Stack;

public class MyListener extends LITTLEBaseListener{
    private Stack symbolTableStack;
    private Stack scopeStack;
    private String currentType;
    private String type;
    private HashMap values;
    private String lastName;
    private int blockNumber;

    MyListener(){
        symbolTableStack = new Stack<HashMap>();
        scopeStack = new Stack<String>();
        values = new HashMap<String, String>();
        blockNumber = 1;
        type = "";
        lastName = "";
    }

    private void printTable(HashMap symbolTabel){
        symbolTabel.forEach((k,v) -> {
            System.out.print("name "+k+" type "+v);
            if(v == "STRING"){
                System.out.println(" value " + values.get(k));
            }else {
                System.out.println();
            }
        });
    }


    @Override public void enterProgram(LITTLEParser.ProgramContext ctx) {
        scopeStack.push("GLOBAL");
        System.out.println("Symbol table GLOBAL");
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
        System.out.println();
    }

    @Override public void enterIf_stmt(LITTLEParser.If_stmtContext ctx) {
        scopeStack.push("BLOCK " + blockNumber);
        System.out.println();
        System.out.println("Symbol table BLOCK " + blockNumber);
        symbolTableStack.push(new HashMap<String, String>());
        blockNumber++;
    }

    @Override public void enterElse_part(LITTLEParser.Else_partContext ctx) {
        scopeStack.pop();
        symbolTableStack.pop();
        scopeStack.push("BLOCK" + blockNumber);
        System.out.println();
        System.out.println("Symbol table BLOCK " + blockNumber);
        symbolTableStack.push(new HashMap<String, String>());
        blockNumber++;
    }
    @Override public void exitIf_stmt(LITTLEParser.If_stmtContext ctx) {
        scopeStack.pop();
        symbolTableStack.pop();
    }

    @Override public void enterFunc_declarations(LITTLEParser.Func_declarationsContext ctx) {
//        if(scopeStack.contains("GLOBAL")){
//            System.out.println("Symbol table " + scopeStack.pop());
//        }
    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        type = "funct";
        symbolTableStack.push(new HashMap<String, String>());
    }

    @Override public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
        scopeStack.pop();
        symbolTableStack.pop();
    }

    @Override public void enterVar_type(LITTLEParser.Var_typeContext ctx) {
        currentType = ctx.getText();
    }

    @Override public void enterString_decl(LITTLEParser.String_declContext ctx) {
        currentType = "STRING";
        type = "str";
    }
    @Override public void enterStr(LITTLEParser.StrContext ctx) {
        System.out.print(" value "+ ctx.getText());
        System.out.println();
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
                //FUCK
            }
            symbolTable.put(name, currentType);
            System.out.print("name "+name+" type "+currentType);
        }else if(type.equals("funct")){
            scopeStack.push(name);
            System.out.println();
            System.out.println("Symbol table " + name);
        }
        if(type.equals("varDec")){
            System.out.println();
        }
    }
}
