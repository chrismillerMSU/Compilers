import java.util.*;

import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends LITTLEBaseListener{
    private boolean splitting = false;
    private boolean splitEnded = false;
    private int tempNumber = 0;
    private int assemblyNumber = -1;
    private ASTNode root;
    private ASTNode curChild = null;
    private ASTNode rootNode;
    private Queue<ASTNode> symbolTableStack = new LinkedList<ASTNode>();
    private HashMap<String,String> symbolTable;
    private HashMap<String,String> stringValues;
    HashSet<Integer> registers = new HashSet<Integer>();
    AssemblyBuilder ir;

    ASTBuilder(HashMap<String,String> symbolTable, HashMap<String,String> stringValues){
        this.symbolTable = symbolTable;
        this.stringValues = stringValues;
        this.ir = new AssemblyBuilder(symbolTable, stringValues);
    }
    @Override public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        char var = ctx.getText().charAt(0);
        String value = ctx.getText().split("=")[1].split(";")[0];
        ASTNode expr = new ASTNode("=");
        ASTNode varAssigned = new ASTNode(Character.toString(var), symbolTable.get(Character.toString(var)),value);
        expr.add_expr(varAssigned);
        root = new ASTNode("=");
        root.addRightChild(new ASTNode(Character.toString(var)));
        curChild = root;
        symbolTableStack.add(expr);
    }
    @Override public void exitAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        while(symbolTableStack.size() > 0) {
            ASTNode node = symbolTableStack.poll();
            ir.addNode(node);
        }
        tempNumber = ir.getRegisterCounter()-1;
        printCode();
    }

    @Override public void enterWrite_stmt(LITTLEParser.Write_stmtContext ctx) {
        String var = ctx.getText().split("\\(")[1].split("\\)")[0];
        for(String value : var.split(",")) {
            value = value.trim();
            ASTNode expr = new ASTNode("WRITE");
            ASTNode varWritten = new ASTNode(value, symbolTable.get(value),null);
            expr.add_expr(varWritten);
            symbolTableStack.add(expr);
        }
    }

    @Override public void enterRead_stmt(LITTLEParser.Read_stmtContext ctx) {
        String var = ctx.getText().split("\\(")[1].split("\\)")[0];
        for(String value : var.split(",")) {
            value = value.trim();
            ASTNode expr = new ASTNode("READ");
            ASTNode varWritten = new ASTNode(value, symbolTable.get(value),null);
            expr.add_expr(varWritten);
            symbolTableStack.add(expr);
        }
    }

    @Override public void enterFunc_decl(LITTLEParser.Func_declContext ctx) {
        String name = ctx.getText().split("\\(")[0];
        name = name.split("FUNCTIONVOID")[1];
        ASTNode expr = new ASTNode("LABEL");
        ASTNode funcName = new ASTNode(name, "FUNC",null);
        expr.add_expr(funcName);
        symbolTableStack.add(expr);
    }

    @Override public void exitProgram(LITTLEParser.ProgramContext ctx) {
        while(symbolTableStack.size() > 0) {
            ASTNode node = symbolTableStack.poll();
            ir.addNode(node);
        }
        //System.out.println(";RET");
        //System.out.println(";tiny code");
        System.out.println("sys halt");
    }



    @Override public void exitExpr_prefix(LITTLEParser.Expr_prefixContext ctx) {
        boolean rightSide = true;
        String right = "tooLong";
        String op = "tooLong";
        String left= "tooLong";
        try {
            LITTLEParser.ExprContext parent;
            parent = (LITTLEParser.ExprContext) ctx.parent;
            right = parent.factor().getText();
            op= ctx.addop().getText();
            left= ctx.factor().getText();
        }catch (Exception e){
            rightSide = false;
        }
        if(!rightSide) {
            try {
                LITTLEParser.Expr_prefixContext parent;
                parent = (LITTLEParser.Expr_prefixContext) ctx.parent;
                right = parent.factor().getText();
                op= ctx.addop().getText();
                left= ctx.factor().getText();
            } catch (Exception e) {
            }
        }
        if(op.length()==1) {
            addNode(right,left,op);
        }
    }

    @Override public void exitFactor_prefix(LITTLEParser.Factor_prefixContext ctx) {
        boolean rightSide = true;
        String op = "tooLong";
        String left = "tooLong";
        String right = "tooLong";
        try {
            LITTLEParser.FactorContext parent;
            parent = (LITTLEParser.FactorContext) ctx.parent;
            right = parent.postfix_expr().getText();
            op = ctx.mulop().getText();
            left = ctx.postfix_expr().getText();
        }catch (Exception e){
            rightSide = false;
        }
        if(!rightSide) {
            try {
                LITTLEParser.Factor_prefixContext parent;
                parent = (LITTLEParser.Factor_prefixContext) ctx.parent;
                right = parent.postfix_expr().getText();
                op = ctx.mulop().getText();
                left = ctx.postfix_expr().getText();
            } catch (Exception e) {
            }
        }
        if(op.length() == 1) {
            addNode(right,left,op);
        }
    }

    private String getType(String input){
        try{
            int i = Integer.parseInt(input);
            return  "int";
        }catch (NumberFormatException e){

        }
        try{
            Float i = Float.parseFloat(input);
            return  "float";
        }catch (NumberFormatException e){

        }
        if(input.equals("+") || input.equals("-")||input.equals("*")||input.equals("/")||input.equals("="))return "op";
        else if(input.length() == 1)return "var";
        return "none";
    }

    private void addNode(String right,String left,String op){
        String rightType = getType(right);
        String leftType = getType(left);
        ASTNode newNode = new ASTNode(op);

        if(!(leftType.equals("none")) && curChild.equals(root) && rightType.equals("none")){
            curChild.addLeftChild(newNode);
            newNode.addParent(curChild);

            ASTNode firstChar = new ASTNode(left);
            newNode.addRightChild(firstChar);
            firstChar.addParent(newNode);

            curChild = newNode;
            return;
        }
        if (!(rightType.equals("none")) && !(leftType.equals("none"))) {
            if(curChild.getLeftChild()==null){
                newNode.addParent(curChild);
                curChild.addLeftChild(newNode);
                curChild = newNode;
            }else if(curChild.getrightChild() != null && left.equals(curChild.getrightChild().getName()) && sameKindOfOp(curChild.getName(), op)){
                ASTNode parent = curChild.getParent();
                splitting = true;
                if(parent.getChildSide(curChild)){
                    parent.addLeftChild(newNode);
                    ASTNode rightNode = new ASTNode(right);
                    rightNode.addParent(newNode);
                    newNode.addRightChild(rightNode);
                }else{
                    parent.addRightChild(newNode);
                    ASTNode rightNode = new ASTNode(right);
                    rightNode.addParent(newNode);
                    newNode.addRightChild(rightNode);
                }
                newNode.addParent(parent);
                curChild.addParent(newNode);
                newNode.addLeftChild(curChild);
                curChild = newNode;
                return;

            } else if(splitEnded){
                splitEnded = false;
                if(curChild.getParent() != null) {
                    ASTNode dupe = new ASTNode(curChild.getName());
                    dupe.addParent(curChild.getParent());
                    if (curChild.getParent().getChildSide(curChild)) {
                        curChild.getParent().addLeftChild(dupe);
                    } else {
                        curChild.getParent().addRightChild(dupe);
                    }
                    curChild.addParent(dupe);
                    dupe.addLeftChild(curChild);
                    ASTNode leftNode = new ASTNode(left);
                    leftNode.addParent(newNode);
                    newNode.addLeftChild(leftNode);
                    ASTNode rightNode = new ASTNode(right);
                    rightNode.addParent(newNode);
                    newNode.addRightChild(rightNode);
                    dupe.addRightChild(newNode);
                    newNode.addParent(dupe);
                    curChild = dupe;
                    return;
                }
            } else{
                newNode.addParent(curChild);
                curChild.addRightChild(newNode);
                curChild = newNode;
            }
            ASTNode leftNode = new ASTNode(left);
            leftNode.addParent(curChild);
            curChild.addLeftChild(leftNode);

            ASTNode rightNode = new ASTNode(right);
            rightNode.addParent(curChild);
            curChild.addRightChild(rightNode);
        } else if (!(leftType.equals("none"))) {
            if(this.splitting) {
                splitting = false;
                this.splitEnded = true;
                return;
            }
            addOneChild(left);
        } else if (!(rightType.equals("none"))) {
            try {
                ASTNode rightNode = new ASTNode(right);
                ASTNode parent = curChild.getParent();
                newNode.addRightChild(rightNode);
                rightNode.addParent(newNode);
                newNode.addLeftChild(curChild);
                curChild.addParent(newNode);
                boolean side = parent.getChildSide(curChild);
                if (side) {
                    parent.addLeftChild(newNode);
                } else {
                    parent.addRightChild(newNode);
                }
                newNode.addParent(parent);
                curChild = newNode;
            }catch (Exception e){

            }

        } else {
            try {
                ASTNode parent;
                if(curChild.getParent().getParent() !=null) {
                    parent = curChild.getParent().getParent();
                    curChild = curChild.getParent();
                }else{
                    parent = curChild.getParent();
                }
                boolean side = parent.getChildSide(curChild);
                if (side) {
                    parent.addLeftChild(newNode);
                } else {
                    parent.addRightChild(newNode);
                }
                newNode.addParent(parent);
                newNode.addLeftChild(curChild);
                curChild.addParent(newNode);
                curChild = newNode;
            }catch (Exception e){

            }
        }
    }

    private boolean sameKindOfOp(String currOp, String newOp){
        if((currOp.equals("+")||currOp.equals("-")) &&(newOp.equals("+")||newOp.equals("-"))){
            return true;
        }else return (currOp.equals("*") || currOp.equals("/")) && (newOp.equals("*") || newOp.equals("/"));
    }

    private void printInfo(){
        try {
            System.out.println("Name: " + curChild.getName());
            System.out.println("Parent: " + curChild.getParent().getName());
            if(curChild.getParent().getChildSide(curChild)){
                System.out.println("Left Child");
            }else{
                System.out.println("Right Child");
            }
            if(curChild.getParent().getrightChild() != null){
                System.out.println("Parent Right Child: " + curChild.getParent().getrightChild().getName());
            }
            if(curChild.getrightChild() ==null){
                System.out.println("Right: None");
            }else{
                System.out.println("Right: " + curChild.getrightChild().getName());
            }
            if(curChild.getLeftChild() ==null){
                System.out.println("Left: None");
            }else{
                System.out.println("Left: " + curChild.getLeftChild().getName());
                if(curChild.getLeftChild().getLeftChild() != null){
                    System.out.println("Left's left child: "+ curChild.getLeftChild().getLeftChild().getName());
                }if(curChild.getLeftChild().getrightChild() != null){
                    System.out.println("Left's right child: "+ curChild.getLeftChild().getrightChild().getName());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addOneChild(String name){
        ASTNode newNode = new ASTNode(name);
        if(curChild.getrightChild()==null){
            curChild.addRightChild(newNode);
        }else{
            curChild.addLeftChild(newNode);
        }
        newNode.addParent(curChild);
    }

    private void printAST(){
        ASTNode counter = root;
        int count = 0;
        int count2 = 0;
        ArrayList<ASTNode> visited = new ArrayList<>();
        System.out.println("PRINTING AST:");
        do{
            count2++;
            String name = counter.getName();
            String rightChildName;
            String leftChildName;
            ASTNode leftChild = counter.getLeftChild();
            ASTNode rightChild = counter.getrightChild();

            if(!visited.contains(counter)){
                System.out.println("Node " + count+" :"+ name);
                visited.add(counter);
                count++;
                if(leftChild != null ){
                    leftChildName = leftChild.getName();
                    System.out.println("Left Child: " + leftChildName);
                }else{
                    System.out.println("Left Child: None" );
                }
                if(rightChild!=null){
                    rightChildName = rightChild.getName();
                    System.out.println("Right Child: " + rightChildName);
                }else{
                    System.out.println("Right Child: None");
                    System.out.println();
                }
            }

            if((leftChild ==null && rightChild == null) || (visited.contains(leftChild) && visited.contains(rightChild))){
                counter = counter.getParent();
            }else if(leftChild != null && !visited.contains(leftChild)){
                counter = counter.getLeftChild();
            }else if(rightChild != null && !visited.contains(rightChild)){
                counter = counter.getrightChild();
            }
        }while(counter != null && count2<100);
    }

    private void printCode(){
        ASTNode counter = root;
        if(counter.getLeftChild() == null)return; //If var declaration
        float total = 0;
        int count = 0;
        int count2 = 0;
        HashMap<ASTNode, Integer> visited = new HashMap<>();
        Stack<String> opStack = new Stack<>();
        Stack<String> varStack = new Stack<>();
        do{
            count2++;
            String name = counter.getName();
            String type = getType(name);
            String rightChildName;
            String leftChildName;
            ASTNode leftChild = counter.getLeftChild();
            ASTNode rightChild = counter.getrightChild();

            if(!visited.containsKey(counter)){
                visited.put(counter,1);
                if(type.equals("op")){
                    opStack.push(name);
                }else if(type.equals("var")||type.equals("int")||type.equals("float")){
                    //if(varStack.size()>1)System.out.println(varStack.peek());
                    varStack.push(name);
                    if(counter.getParent() != null &&!counter.getParent().getChildSide(counter)){ //if right child
                        varStack.push(printOperation(opStack.pop(),varStack.pop(),varStack.pop(), "r"));
                    }else if(counter.getParent() == null){ //root
                        printOperation(opStack.pop(),varStack.pop(),varStack.pop(), "r");
                        //ADD TO IR BUILDER addNode();
                    }
                }
            }else if(visited.get(counter)>1 && type.equals("op") && getType(leftChild.getName()).equals("op") && getType(rightChild.getName()).equals("op")){
                varStack.push(printOperation(opStack.pop(),varStack.pop(),varStack.pop(), "r"));
            }else if(visited.get(counter)>1 && type.equals("op") && !getType(leftChild.getName()).equals("op") && getType(rightChild.getName()).equals("op")) {
                varStack.push(printOperation(opStack.pop(),varStack.pop(),varStack.pop(), "r"));
            }else{
                visited.replace(counter,1,2); //If we visit twice
            }

            if((leftChild ==null && rightChild == null) || (visited.containsKey(leftChild) && visited.containsKey(rightChild))){
                counter = counter.getParent();
            }else if(leftChild != null && !visited.containsKey(leftChild)){
                counter = counter.getLeftChild();
            }else if(rightChild != null && !visited.containsKey(rightChild)){
                counter = counter.getrightChild();
            }
        }while(counter != null && count2<100);
    }
//
    private String printOperation(String op, String right, String left, String registerType){
        tempNumber++;
        if(registers.contains(tempNumber)) {
            tempNumber = Collections.max(registers);
        }
        ir.addComplexNode(op, right, left, registerType + tempNumber);
        registers.add(tempNumber);
        ir.setRegisterCounter(tempNumber);
        return registerType + tempNumber;
    }
}



