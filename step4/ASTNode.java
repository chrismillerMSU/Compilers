import java.util.ArrayList;
import java.util.LinkedList;

class ASTNode {
	public String varName;
	public String type;
	public boolean isExpr;
	public String value;
	private ASTNode leftChild = null;
	private ASTNode rightChild = null;
	private ASTNode parent = null;
	public LinkedList<ASTNode> children = new LinkedList<ASTNode>();
	
	/** create an AST node for an expression. i.e. MulExpr. These have no variables, but do have types.
	*	@param exprType, the type of expression, i.e. Mul, Add, Sub, Div...
	*/
	public ASTNode(String exprType) {
		this.type = exprType;
		this.isExpr = true;
		this.varName = "";
		this.value = null;
	}
	
	/** create an AST node for an a variable. i.e. int a, int b, string c.
	*	@param varName, the name of the variable
	*	@param varType, the type of variable, i.e. int, float, string...
	*/
	public ASTNode(String varName, String varType, String value) {
		this.type = varType;
		this.varName = varName;
		this.isExpr = false;
		this.value = value;
	}
	
	/** add children to ASTNode. This could either be a variable, or an expression.
	*	@param node, the ASTNode to add.
	*/
	public void add_expr(ASTNode node) {
		this.children.add(node);
	}
	public void addRightChild(ASTNode node) {
		this.rightChild = node;
	}
	public void addLeftChild(ASTNode node) {
		this.leftChild = node;
	}

	public void addParent(ASTNode node){
		this.parent = node;
	}

	public ASTNode getLeftChild(){return leftChild;}

	public ASTNode getrightChild(){return rightChild;}

	public ASTNode getParent(){return parent;}

	public boolean leftChildExists(){return leftChild != null;}
	public boolean rightChildExists(){return rightChild != null;}

	public boolean getChildSide(ASTNode node){
		if(leftChild != null && this.leftChild.equals(node)){
			return true;
		}else{
			return false;
		}
	}


	public String getName(){return this.type;}
}