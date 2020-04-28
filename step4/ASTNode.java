class ASTNode {
	String op;
	String type;
	boolean isExpr;
	ArrayList<ASTNode> children = new ArrayList<ASTNode>();
	public ASTNode(String op, String exprType, Boolean isExpr) {
		this.type = exprType;
		this.isExpr = true;
		this.op = null;
	}
	public ASTNode(String op, String opType) {
		this.type = opType;
		this.op = op;
		this.isExpr = false;
	}
	public void add_op(ASTNode node) {
		this.children.add(node);
	}
}