import java.util.HashMap;
public class IRBuilder {

    public static int registerCounter = 1;
    HashMap<String, String> registers = new HashMap<String, String>();

    public IRBuilder() {
//        System.out.println(";IR code");
    }

    public void addComplexNode(String op, String right, String left) {
        String opType = setOp(op);
        if(opType.equals("ADD")) {
            String newRight = registers.containsKey(right) ? registers.get(right) : right;
            String newLeft = registers.containsKey(left) ? registers.get(left) : left;

//            registers.put("$T" + registerCounter, child.varName);
//                System.out.println("ADDI " + child.value + " $T" + registerCounter);
//                registerCounter++;
        } else if(opType.equals("MULTI")) {

        } else if(opType.equals("SUB")) {

        } else if(opType.equals("DIVI")) {

        }
    }

    public void addNode(ASTNode node) {
        String op = setOp(node.type);
        if(op.equals("STORE")) {
            while(node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                //NEED TO ACCOUNT FOR ALREADY USED REGISTERS.
                //IF IT HAS VARIABLES, THEN JUST DO SECOND STORE WITH REGISTER FROM MULT, ADD, SUB, DIVI OPERATION.
                if(!child.value.contains("+") && !child.value.contains("-") && !child.value.contains("*") && !child.value.contains("/")) {
                    //---------------------------------
                    registers.put("$T" + registerCounter, child.varName);
                    System.out.println(";STORE" + type + " " + child.value + " $T" + registerCounter);
                    System.out.println(";STORE" + type + " " + "$T" + registerCounter + " " + child.varName);
                    registerCounter++;
                }
            }
        } else if(op.equals("WRITE") || op.equals("READ")) {
            while (node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                System.out.println(";" + op + type +  " " + child.varName);
            }
        } else if(op.equals("LABEL")) {
            while (node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                System.out.println(";IR code");
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