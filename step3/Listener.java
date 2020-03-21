public class Listener extends LITTLEBaseListener {
	SymbolTable st;
	Stack<SymbolTable> stack;
	public Listener() {
		st = new SymbolTable();
		stack = new Stack<SymbolTable>;
	}
	
	public Stack<SymbolTable> getSymbolTable() {
	
	}
	
	@Override
	public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
		//operate on symbol table here...
	}
	
	@Override
	public void exitFunc_decl(LITTLEParser.Func_declContext ctx) {
		//operate on symbol table here...
	}
}