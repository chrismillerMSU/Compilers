import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class ASTBuilder extends LITTLEBaseListener{
    private ASTNode rootNode;
    private Queue<ASTNode> symbolTableStack = new LinkedList<ASTNode>();
    private HashMap<String,String> symbolTable;

    ASTBuilder(HashMap<String,String> symbolTable){
        this.symbolTable = symbolTable;
    }

    @Override public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        char var = ctx.getText().charAt(0);
        String value = ctx.getText().split("=")[1].split(";")[0];
        System.out.println(var + " VALUE: " + value);
        ASTNode expr = new ASTNode("=");
        ASTNode varAssigned = new ASTNode(Character.toString(var), symbolTable.get(Character.toString(var)),value);
        expr.add_expr(varAssigned);

        //-------------------------
        //ADD SOME CODE FOR CHECKING FOR ARITHMETIC OPERATIONS...



        //-------------------------
        symbolTableStack.add(expr);
    }

    @Override public void enterWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
        String var = ctx.getText().split("\\(")[1].split("\\)")[0];
        for(String value : var.split(",")) {
            value = value.trim();
            System.out.println("WRITING: " + value);
            ASTNode expr = new ASTNode("WRITE");
            ASTNode varWritten = new ASTNode(value, symbolTable.get(value),null);
            expr.add_expr(varWritten);
            symbolTableStack.add(expr);
        }
    }

    @Override public void enterRead_stmt(LITTLEParser.Read_stmtContext ctx) {
        String var = ctx.getText().split("\\(")[1].split("\\)")[0];
        for(String value : var.split(",")) {
            value = value.trim();
            System.out.println("READING: " + value);
            ASTNode expr = new ASTNode("READ");
            ASTNode varWritten = new ASTNode(value, symbolTable.get(value),null);
            expr.add_expr(varWritten);
            symbolTableStack.add(expr);
        }
    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        String name = ctx.getText().split("\\(")[0];
        name = name.split("FUNCTIONVOID")[1];
        System.out.println("Function name: " + name);
        ASTNode expr = new ASTNode("LABEL");
        ASTNode funcName = new ASTNode(name, "FUNC",null);
        symbolTableStack.add(expr);
    }

    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
        //on exit program print the AST...
        System.out.println("PRINT THE AST");
        IRBuilder ir = new IRBuilder();
        while(symbolTableStack.size() > 0) {
            ASTNode node = symbolTableStack.poll();
            ir.addNode(node);
            //
        }
    }

}
