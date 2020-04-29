import java.util.HashMap;
public class IRBuilder {

    public static int registerCounter = 1;
    HashMap<String, String> registers = new HashMap<String, String>();

    public IRBuilder() {
        System.out.println(";IR code");
    }

    public void addNode(ASTNode node) {
        String op = setOp(node.type);
        if(op.equals("STORE")) {
            while(node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                //NEED TO ACCOUNT FOR ALREADY USED REGISTERS.
                registers.put("$T" + registerCounter, child.varName);
                System.out.println(";STORE"+type+ " " + child.value + " $T" + registerCounter);
                System.out.println(";STORE"+type+ " " + "$T" + registerCounter + " " + child.varName);
                registerCounter++;
            }
        } else if(op.equals("ADD")) {
            while(node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                System.out.println(child.varName);
//                registers.put("$T" + registerCounter, child.varName);
//                System.out.println("ADDI " + child.value + " $T" + registerCounter);
//                registerCounter++;
            }
        } else if(op.equals("MULTI")) {

        } else if(op.equals("SUB")) {

        } else if(op.equals("DIVI")) {

        } else if(op.equals("WRITE") || op.equals("READ")) {
            while (node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                System.out.println(";" + op + type +  " " + child.varName);
            }
        } else if(op.equals("LABEL")) {
            while (node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                System.out.println(";LABEL " + child.varName);
                System.out.println(";LINK");
            }
        }
    }

    public String setType(String value) {
        String type = "";
        switch(value) {
            case "INT":
                type = "I";
                break;
            case "STRING":
                type = "S";
                break;
            case "FLOAT":
                type = "F";
                break;
            default:
                break;
        }
        return type;
    }

    public String setOp(String varType) {
        String op = "";
        switch (varType) {
            case "=":
                op = "STORE";
                break;
            case "+":
                op = "ADD";
                break;
            case "-":
                op = "SUB";
                break;
            case "*":
                op = "MULTI";
                break;
            case "/":
                op = "DIVI";
                break;
            default:
                op = varType;
                break;
        }
        return op;
    }
}