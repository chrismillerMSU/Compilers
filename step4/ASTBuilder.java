import java.util.HashMap;

public class ASTBuilder extends LITTLEBaseListener{
    private ASTNode rootNode;
    private HashMap<String,String> symbolTable;
    ASTBuilder(HashMap<String,String> symbolTable){
        this.symbolTable = symbolTable;
    }

    @Override public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        char var = ctx.getText().charAt(0);
        System.out.println(var);
        rootNode = new ASTNode("=");

    }
}
