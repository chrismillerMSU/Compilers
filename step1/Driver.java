import java.io.*;
import org.antlr.v4.runtime.*;

public class Driver {

	public static void main(String[] args) throws Exception{
		try{
			LITTLE lexer = new LITTLE(CharStreams.fromFileName(args[0]));
			Vocabulary vocab = lexer.getVocabulary();
			Token token = lexer.nextToken();
			while(true) {
				if(token.getType() == Token.EOF){
					break;
				}
				System.out.println("Token Type: " + vocab.getSymbolicName(token.getType()));
				System.out.println("Value: " + token.getText());
				token = lexer.nextToken();
			}
		} catch (IOException e) {
			System.out.println("Lexing failed: " + e);
		}
	}
}
