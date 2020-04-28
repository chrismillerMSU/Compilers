import java.util.ArrayList;

class ASTNode {
	private String varName;
	private String type;
	private boolean isExpr;
	private ArrayList<ASTNode> children = new ArrayList<ASTNode>();
	
	/** create an AST node for an expression. i.e. MulExpr. These have no variables, but do have types.
	*	@param exprType, the type of expression, i.e. Mul, Add, Sub, Div...
	*/
	public ASTNode(String exprType) {
		this.type = exprType;
		this.isExpr = true;
		this.varName = null;
	}
	
	/** create an AST node for an a variable. i.e. int a, int b, string c.
	*	@param varName, the name of the variable
	*	@param varType, the type of variable, i.e. int, float, string...
	*/
	public ASTNode(String varName, String varType) {
		this.type = varType;
		this.varName = varName;
		this.isExpr = false;
	}
	
	/** add children to ASTNode. This could either be a variable, or an expression.
	*	@param node, the ASTNode to add.
	*/
	public void add_expr(ASTNode node) {
		this.children.add(node);
	}
}