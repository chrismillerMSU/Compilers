import java.io.*; 
import java.util.*; 
public class Listener extends LITTLEBaseListener {
	public SymbolTable st;
	public Stack<SymbolTable> stack;
	public Listener() {
		st = new SymbolTable();
		stack = new Stack<SymbolTable>();
	}
	
	public SymbolTable getSymbolTable() {
		return st;
	}
	
	@Override
	public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
		//operate on symbol table here...
		System.out.println("ENTERED");
	}
	
	@Override
	public void exitFunc_decl(.Func_declContext ctx) {
		//operate on symbol table here...
		System.out.println("EXITED");
	}
	
	@Override
	public void enterVar_decl(LITTLEParser.Var_declContext ctx) {
		
	}
	
	@Override
	public void enterString_decl(LITTLEParser.String_declContext ctx) {
		System.out.println("STR");
	}
}