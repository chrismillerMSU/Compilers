import java.util.HashMap;
import java.util.Map;

public class AssemblyBuilder {

    private static int registerCounter = 0;
    HashMap<String, String> registers = new HashMap<String, String>();
    private HashMap<String,String> symbolTable;
    private HashMap<String,String> stringValues;

    public AssemblyBuilder(HashMap<String, String> symbolTable, HashMap<String,String> stringValues) {
        this.symbolTable = symbolTable;
        this.stringValues = stringValues;
        for (Map.Entry mapElement : symbolTable.entrySet()) {
            String key = (String)mapElement.getKey();
            if(mapElement.getValue().equals("STRING")){
                System.out.println("str " + key + " " + stringValues.get(key));
            } else {
                System.out.println("var " + key);
            }
        }
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
            System.out.println("move " + left + " " + register);
            System.out.println("add" + type + " " + right + " " + register);
        } else if(opType.equals("MULTI")) {
            System.out.println("move " + left + " " + register);
            System.out.println("mul" + type + " " + right + " " + register);
        } else if(opType.equals("SUB")) {
            System.out.println("move " + left + " " + register);
            System.out.println("sub" + type + " " + right + " " + register);
        } else if(opType.equals("DIVI")) {
            System.out.println("move " + left + " " + register);
            System.out.println("div" + type + " " + right + " " + register);
        } else if(opType.equals("STORE")) {
            System.out.println("move " + left + " " + right);
        }
    }

    public void addNode(ASTNode node) {
        String op = setOp(node.type);
        if(op.equals("STORE")) {
            while(node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                if(!child.value.contains("+") && !child.value.contains("-") && !child.value.contains("*") && !child.value.contains("/")) {
                    System.out.println("move " + child.value + " r" + registerCounter);
                    System.out.println("move " + "r" + registerCounter + " " + child.varName);
                    registerCounter++;
                }
            }
        } else if(op.equals("WRITE") || op.equals("READ")) {
            while (node.children.size() > 0) {
                ASTNode child = node.children.removeFirst();
                String type = setType(child.type);
                System.out.println("sys " + op.toLowerCase() + type +  " " + child.varName);
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
                type = "i";
                break;
            case "STRING":
                type = "s";
                break;
            case "FLOAT":
                type = "r";
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