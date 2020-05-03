import java.util.HashMap;
public class IRBuilder {

    private static int registerCounter = 1;
    HashMap<String, String> registers = new HashMap<String, String>();
    private HashMap<String,String> symbolTable;

    public IRBuilder(HashMap<String, String> symbolTable) {
        System.out.println(";IR code");
        this.symbolTable = symbolTable;
    }

    public void addComplexNode(String op, String right, String left, String register) {
        String opType = setOp(op);
        String type = "";
        if(symbolTable.get(right) != null) type = symbolTable.get(right);
        if(symbolTable.get(left) != null) type = symbolTable.get(left);
        type = setType(type);
        registers.put(register, type);

        if(type.length() == 0) {
            if(registers.get(left) != null) type = registers.get(left);
            if(registers.get(right)!= null && type.length() == 0) type = registers.get(right);
        }

        if(type.length() == 0) {
            type = getStaticType(left);
            if(type.length() == 0) {
                type = getStaticType(right);
            }
        }

        if(opType.equals("ADD")) {
            System.out.println(";ADD" + type + " " + left + " " + right + " " + register);
        } else if(opType.equals("MULTI")) {
            System.out.println(";MULT" + type + " " + left + " " + right + " " + register);
        } else if(opType.equals("SUB")) {
            System.out.println(";SUB" + type + " " + left + " " + right + " " + register);
        } else if(opType.equals("DIVI")) {
            System.out.println(";DIV" + type + " " + left + " " + right + " " + register);
        } else if(opType.equals("STORE")) {
            System.out.println(";STORE" + type + " " + left + " " + right);
        }
    }

    public void addNode(ASTNode node) {
        String op = setOp(node.type);
        if(op.equals("STORE")) {
            while(node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                if(!child.value.contains("+") && !child.value.contains("-") && !child.value.contains("*") && !child.value.contains("/")) {
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

    private String getStaticType(String input){
        try{
            int i = Integer.parseInt(input);
            return  "I";
        }catch (NumberFormatException e){

        }
        try{
            Float i = Float.parseFloat(input);
            return  "F";
        }catch (NumberFormatException e){

        }
        return "S";
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

    public int getRegisterCounter() {
        return registerCounter;
    }

    public void setRegisterCounter(int tempNumber) {
        registerCounter = tempNumber;
    }
}