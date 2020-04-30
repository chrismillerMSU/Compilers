import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends LITTLEBaseListener{
    private boolean splitting = false;
    private boolean splitEnded = false;
    private ASTNode root;
    private ASTNode curChild = null;
    private LITTLEParser parser;
    ASTBuilder(LITTLEParser parser){
        this.parser = parser;
    }
    @Override public void enterAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        char var = ctx.getText().charAt(0);
        root = new ASTNode("=");
        root.addRightChild(new ASTNode(Character.toString(var)));
        curChild = root;
        System.out.println("Root right " + root.getrightChild().getName());
    }
    @Override public void exitAssign_stmt(LITTLEParser.Assign_stmtContext ctx) {
        printAST();
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
            System.out.println("E op: "+op +"__"+ op.length());
            System.out.println("E left: "+left+"__"+ left.length());
            System.out.println("E right: "+right+"__"+ right.length());
            addNode(right,left,op);
        }

        System.out.println();
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
            //System.out.println(e.getMessage());
        }
        if(!rightSide) {
            try {
                LITTLEParser.Factor_prefixContext parent;
                parent = (LITTLEParser.Factor_prefixContext) ctx.parent;
                right = parent.postfix_expr().getText();
                op = ctx.mulop().getText();
                left = ctx.postfix_expr().getText();
            } catch (Exception e) {
                //System.out.println(e.getMessage());
            }
        }
        if(op.length() == 1) {
            System.out.println("F op: "+op);
            System.out.println("F left: "+left);
            System.out.println("F right: "+right);
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
        if(input.length() == 1)return "var";
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
                System.out.println("curr left child: none");
                newNode.addParent(curChild);
                curChild.addLeftChild(newNode);
                curChild = newNode;
            }else if(curChild.getrightChild() != null && left.equals(curChild.getrightChild().getName()) && curChild.getName().equals(op)){
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
                printInfo();
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
                    System.out.println("Right Child p= " + rightNode.getParent().getName());
                    return;
                }
            } else{
                System.out.println("curr left child: "+ curChild.getLeftChild().getName());
                newNode.addParent(curChild);
                curChild.addRightChild(newNode);
                curChild = newNode;
                System.out.println("curr Node: "+ curChild.getName());
                System.out.println("parent left child: "+ curChild.getParent().getLeftChild().getName());
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
        printInfo();
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
}



