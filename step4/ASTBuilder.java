import java.util.HashMap;
import java.util.Stack;

public class ASTBuilder extends LITTLEBaseListener{
    private ASTNode rootNode;
    private Stack<ASTNode> symbolTableStack = new Stack<ASTNode>();
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
        symbolTableStack.add(expr);
    }

    @Override public void enterWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
        char var = ctx.getText().charAt(6);
        System.out.println("WRITING: " + var);
        ASTNode expr = new ASTNode("WRITE");
        ASTNode varWritten = new ASTNode(Character.toString(var), symbolTable.get(Character.toString(var)),null);
        expr.add_expr(varWritten);
        symbolTableStack.add(expr);
    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        String name = ctx.getText().split("\\(")[0];
        name = name.split("FUNCTIONVOID")[1];
        System.out.println("Function name: " + name);
        ASTNode expr = new ASTNode("LABEL");
        ASTNode funcName = new ASTNode(name, "FUNC",null);
        symbolTableStack.add(expr);
    }
}
