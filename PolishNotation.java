/**
 *
 * @author SWAPNIL
 * 06-Oct-2016
 *
 *@additionalAuthor InetZen
 *16-May-2018
 */

import java.util.*;
import java.io.*;

public class PolishNotation {

    /**
     * @param args the command line arguments
     */
    
    final ArrayList operators = new ArrayList(Arrays.asList('+','-','*','/','^'));
    final Map <String,Integer> OperatorPriority = new HashMap <String,Integer> () {{
        put("+",0);
        put("-",0);
        put("*",1);
        put("/",1);
        put("^",2);
    }};
    

    public static void main(String[] args) {
        PolishNotation polish = new PolishNotation();
        polish.calculator();
    } 
    
    public void calculator () {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Escape Character is ']'. Enter your input at the prompt");
        String input;
        try {
            while(!(input = in.readLine()).equals("]")) {
              LinkedList <String> Infix = toInfix(input);
              LinkedList <String> PostfixExpr = toPostfix(Infix);
              System.out.println(evaluatePostfix(PostfixExpr));
            }
        } catch(IOException ex) {ex.printStackTrace();}
    }
    
    private LinkedList toInfix (String expr) {
        LinkedList <String> infixExpression = new LinkedList<>();
        for(int i =0; i < expr.length(); i++) {
            char current = expr.charAt(i);
            if(current == ' ');
            else if(isNum(current)) {
                int numLen = getNumber(expr.substring(i));
                infixExpression.add(expr.substring(i, i+numLen));
                i += numLen - 1;
            }
            else if(isOperator(current)) {
                infixExpression.add(String.valueOf(current));
            }
            else if(current == '(' || current == ')') {
                infixExpression.add(String.valueOf(current));
            }
            else {
                System.err.println("Non Integer value Entered. Fatal Error Program Terminated!");
                System.exit(0);
            }
        }
        infixExpression.add(")");
        return infixExpression;
    }
    
    private LinkedList <String> toPostfix(LinkedList <String> infixExpr) {
        Stack <String> infixOperatorStack = new Stack <> ();
        LinkedList <String> PostfixExpr = new LinkedList <> ();
        
        infixOperatorStack.push("(");  //1. Push '(' to the Stack
        while(!infixExpr.isEmpty()) {
            if(isNum(infixExpr.peek()))
                PostfixExpr.add(infixExpr.remove());
            else if(infixExpr.peek().equals("("))
                infixOperatorStack.push(infixExpr.remove());
            else if(infixExpr.peek().length() == 1 && isOperator(infixExpr.peek().charAt(0))) {
                if(infixOperatorStack.peek().equals("(")) {
                    infixOperatorStack.push(infixExpr.remove());
                }
                else {
                    while(OperatorPriority.get(infixExpr.peek()) <= OperatorPriority.get(infixOperatorStack.peek())) {
                        PostfixExpr.add(infixOperatorStack.pop());
                        if(infixOperatorStack.peek().equals("(")) break;
                    }
                    infixOperatorStack.push(infixExpr.remove());
                }
            }
            else if(infixExpr.peek().equals(")")) {
                while(!infixOperatorStack.peek().equals("(")) {
                    PostfixExpr.add(infixOperatorStack.pop());
                }
                infixOperatorStack.pop();
                infixExpr.remove();
            }            
        }
        return PostfixExpr;
    }
    
    private int evaluatePostfix(LinkedList <String> postfixExpr) {
        
        Stack <String> postfixOperandStack = new Stack <> ();
        
        postfixExpr.add(")");
        while(!postfixExpr.peek().equals(")")) {
            if(isNum(postfixExpr.peek())) {
                postfixOperandStack.push(postfixExpr.remove());
            }
            else if (postfixExpr.peek().length() == 1 && isOperator(postfixExpr.peek().charAt(0))) {
                int result = Evaluate(postfixOperandStack.pop(),postfixOperandStack.pop(),postfixExpr.remove().charAt(0));
                postfixOperandStack.push(String.valueOf(result));
            }
        } 
        return Integer.parseInt(postfixOperandStack.pop());
    }
    
    private boolean isNum (char ch) {
        return ch >= '0' && ch <= '9';
    }
    
    private boolean isNum (String num) {
        for(int i = 0; i < num.length(); i++) {
            if(!isNum(num.charAt(i)))
                return false;
        }
        return true;
    }
    
    private boolean isOperator(char ch) {
        return operators.contains(ch);
    }
    
    private int getNumber(String sliceExpr) {
        int i = 0;
        while(i < sliceExpr.length() && isNum(sliceExpr.charAt(i))) 
            i++;
        return i;
    }
    
    private int Evaluate (String first, String second, char operator) {
        int result = 0;
        int A = Integer.parseInt(first);
        int B = Integer.parseInt(second);
        switch(operator) {
            case '+' :
                result = B + A;
                break;
            case '-':
                result = B - A;
                break;
            case '*':
                result = B * A;
                break;
            case '/':
                result = B / A;
                break;
            case '^':
                double resultPow = Math.pow(B,A);
                String imd = String.valueOf(resultPow);
                String split[] = imd.split("\\.");
                result = Integer.parseInt(split[0]);
                break;
        }
        return result;
    }
}
