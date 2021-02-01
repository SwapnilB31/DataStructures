/*
* 
* @author github.com/SwapnilB31
* 06-Oct-2016
*
* Change on 01-Feb-2021
*
* This program is written within the specifications of Nashorn Javascript. Nashorn is a Javascript Engine written in Java
* that ships with the JDK since Java 8.
*
* To run this program, invoke it with the jjs application that you'll find in your jdk/bin
*/

var operators = ["+","-","*","/","^"];

var operatorPrecedence = {
  "+" : 0,
  "-" : 0,
  "*" : 1,
  "/" : 1,
  "^" : 2
};


function isNum(chunk) {
    for(var i in chunk)
      if(!(isNumeral(chunk[i]) || chunk[i] === '.'))
        return false;
      return true;
}

function isNumeral(ele) {
  return ele >= '0' && ele <= '9';
}

function isOperator(ele) {
  for(var i in operators)
    if(ele === operators[i])
      return true;
    return false;
}

function getNumber(sliceExpr) {
  var i = 0;
  while( i < sliceExpr.length && (isNumeral(sliceExpr[i]) || sliceExpr[i] === "."))
    i++;
  return i;
}

function Evaluate (A,B,op) {
  var result = 0;
  switch (op) {
    case "+":
      result = B + A;
      break;
    case "-":
        result = B - A;
        break;
    case "*":
      result = B * A;
      break;
    case "/":
      result = B / A;
      break;
    case "^":
      result = Math.pow(B,A);
  }
  return result;
}

function toInfix (expr) {
  var infixExpression = [];
  for(var i = 0; i < expr.length; i++) {
    if (expr[i] === " ");
    else if (isNumeral(expr[i])) {
      var numLen = getNumber(expr.slice(i));
      infixExpression.push(expr.slice(i,i+numLen));
      i += numLen - 1;
    }
    else if (isOperator(expr[i])) {
      infixExpression.push(expr[i]);
    }
    else if (expr[i] === "(" || expr[i] === ")") {
      infixExpression.push(expr[i]);
    }
    else {
      return false;
    }
  }
  infixExpression.push(")");
  return infixExpression;
}

function toPostfix(infixExpr) {
  var infixOperatorStack = ["("];
  var postfixExpr = [];
  while(infixOperatorStack.length > 0) {
    if (isNum(infixExpr[0])) {
      postfixExpr.push(infixExpr.shift());
    }
    else if (infixExpr[0] === "(") {
      infixOperatorStack.push(infixExpr.shift());
    }
    else if (isOperator(infixExpr[0])) {
      if(infixOperatorStack[infixOperatorStack.length - 1] === "(")
        infixOperatorStack.push(infixExpr.shift());
      else {
        while (operatorPrecedence[infixOperatorStack[infixOperatorStack.length - 1]] >= operatorPrecedence[infixExpr[0]]) {
          postfixExpr.push(infixOperatorStack.pop());
          if(infixOperatorStack[infixOperatorStack.length - 1] === "(") break;
        }
        infixOperatorStack.push(infixExpr.shift());
      }
    }
    else if (infixExpr[0] === ")") {
      while(infixOperatorStack[infixOperatorStack.length - 1] !== "(") {
        postfixExpr.push(infixOperatorStack.pop());
      }
      infixOperatorStack.pop();
      infixExpr.shift();
    }
  }
  return postfixExpr;
}

function evaluatePostfix(postfixExpr) {
  var postfixOperandStack = [];
  postfixExpr.push(")");
  while(postfixExpr[0] !== ")") {
    if(isNum(postfixExpr[0])) {
      postfixOperandStack.push(postfixExpr.shift());
    }
    else if (isOperator(postfixExpr[0])) {
      var result = Evaluate(parseFloat(postfixOperandStack.pop()),parseFloat(postfixOperandStack.pop()),postfixExpr.shift());
      postfixOperandStack.push(result.toString());
    }
  }
  return postfixOperandStack.pop();
}

function calculator() {
  var BufferedReader = Java.type("java.io.BufferedReader");
  var InputStreamReader = Java.type("java.io.InputStreamReader");
  var System = Java.type("java.lang.System");
  var inp = new BufferedReader(new InputStreamReader(System.in));
  try {
    print("The Escape character is ']'. Enter you expression at the prompt");
    var input = inp.readLine();
    while(input != "]") {
      var infix = toInfix(input);
      if(infix === false) {
        print("Non-numerical value entered. Fatal Error Program Terminated!");
        return;
      }
      var postfix = toPostfix(infix);
      print(evaluatePostfix(postfix));
      input = inp.readLine();
    }
  } catch (e) {
    e.printStackTrace();
  }
}

calculator();
