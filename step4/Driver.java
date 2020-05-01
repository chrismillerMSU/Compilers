import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Driver {
	public static boolean error = false;
	public static void main(String[] args) throws Exception{
		try{
			LITTLELexer lexer = new LITTLELexer(CharStreams.fromFileName(args[0]));
			CommonTokenStream tokenStream = new CommonTokenStream(lexer);
			LITTLEParser parser = new LITTLEParser(tokenStream);
			parser.removeErrorListeners();
			parser.addErrorListener(new BaseErrorListener() {
				@Override
				public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
					error = true;
				}
			});
            LITTLEParser.ProgramContext context = parser.program();
			//Step 3
			MyListener listener = new MyListener();
			try {
                new ParseTreeWalker().walk(listener, context);
            }catch (Error e){
			    System.out.println(e.getMessage());
            }
            System.out.println(listener.getSymbolTable());

            ASTBuilder astBuilder = new ASTBuilder(listener.getSymbolTable());
			try {
			    System.out.println("Interative");
				new IterativeParseTreeWalker().walk(astBuilder, context);
			}catch (Error e){
				System.out.println(e.getMessage());
			}


		} catch (IOException e) {
			System.out.println("Error occurred: " + e);
		}
	}
}
