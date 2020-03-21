import java.io.*;
import org.antlr.v4.runtime.*;

public class Driver {
	//public static boolean error = false;
	public static void prettyPrint(SymbolTable s) {
		
	}
	public static void main(String[] args) throws Exception{
		try{
			LITTLELexer lexer = new LITTLELexer(CharStreams.fromFileName(args[0]));
			CommonTokenStream tokenStream = new CommonTokenStream(lexer);
			LITTLEParser parser = new LITTLEParser(tokenStream);
			Listener listener = new Listener();
			new ParseTreeWalker().walk(listener, parser.program());
			SymbolTable s = listener.getSymbolTable();
			prettyPrint(s);
			/*
			parser.removeErrorListeners();
            BaseErrorListener listener = new BaseErrorListener();
			parser.addErrorListener(new BaseErrorListener() {
				@Override
				public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
					error = true;
				}
			});
            parser.program();
			if(error) {
				System.out.println("Not accepted");
			} else {
				System.out.println("Accepted");
			}
			*/
		} catch (IOException e) {
			System.out.println("Error occurred: " + e);
		}
	}
}
