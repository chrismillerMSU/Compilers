class ASTNode {
	String varName;
	String type;
	boolean isExpr;
	ArrayList<ASTNode> children = new ArrayList<ASTNode>();
	
	/** create an AST node for an expression. i.e. MulExpr. These have no variables, but do have types.
	*	@param exprType, the type of expression, i.e. Mul, Add, Sub, Div...
	*	@param isExpr, true if this is an expression.
	*/
	public ASTNode(String exprType, Boolean isExpr) {
		this.type = exprType;
		this.isExpr = true;
		this.varName = null;
	}
	
	/** create an AST node for an a variable. i.e. int a, int b, string c.
	*	@param varName, the name of the variable
	*	@param varType, the type of variable, i.e. int, float, string...
	*/
	public ASTNode(String varName, String varType) {
		this.type = opType;
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