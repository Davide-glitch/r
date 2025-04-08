import java.util.Stack;

public class ArithmeticTree {
    // Node class to represent operators and operands
    private static class Node {
        char operator;
        double operand;
        boolean isOperator;
        Node left, right;
        
        // Constructor for operators
        public Node(char operator) {
            this.operator = operator;
            this.isOperator = true;
        }
        
        // Constructor for operands
        public Node(double operand) {
            this.operand = operand;
            this.isOperator = false;
        }
    }
    
    private Node root;
    
    // Constructors
    public ArithmeticTree() {
        root = null;
    }
    
    public ArithmeticTree(Node root) {
        this.root = root;
    }
    
    // Postorder traversal to generate postfix expression
    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorderHelper(root, sb);
        return sb.toString().trim();
    }
    
    private void postorderHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        
        postorderHelper(node.left, sb);
        postorderHelper(node.right, sb);
        
        if (node.isOperator) {
            sb.append(node.operator).append(" ");
        } else {
            sb.append((int)node.operand).append(" ");
        }
    }
    
    // Calculate the value of the expression
    public double value() {
        return valueHelper(root);
    }
    
    private double valueHelper(Node node) {
        if (node == null) return 0;
        
        if (!node.isOperator) {
            return node.operand;
        }
        
        double leftValue = valueHelper(node.left);
        double rightValue = valueHelper(node.right);
        
        switch (node.operator) {
            case '+': return leftValue + rightValue;
            case '-': return leftValue - rightValue;
            case '*': return leftValue * rightValue;
            case '/': return leftValue / rightValue;
            default: throw new IllegalArgumentException("Unknown operator: " + node.operator);
        }
    }
    
    // Create a tree from postfix expression
    public static ArithmeticTree createFromPostfix(String postfix) {
        if (postfix == null || postfix.trim().isEmpty()) {
            return new ArithmeticTree();
        }
        
        Stack<Node> stack = new Stack<>();
        String[] tokens = postfix.trim().split("\\s+");
        
        for (String token : tokens) {
            if (token.length() == 1 && isOperator(token.charAt(0))) {
                // It's an operator
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression: not enough operands for operator " + token);
                }
                
                Node right = stack.pop();
                Node left = stack.pop();
                Node operator = new Node(token.charAt(0));
                operator.left = left;
                operator.right = right;
                stack.push(operator);
            } else {
                // It's an operand
                try {
                    double value = Double.parseDouble(token);
                    stack.push(new Node(value));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid token in postfix expression: " + token);
                }
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression: too many operands");
        }
        
        return new ArithmeticTree(stack.pop());
    }
    
    // Helper to check if a character is an operator
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    // Create a tree from infix expression
    public static ArithmeticTree createFromInfix(String infix) {
        String postfix = infixToPostfix(infix);
        return createFromPostfix(postfix);
    }
    
    // Convert infix expression to postfix using Shunting Yard algorithm
    private static String infixToPostfix(String infix) {
        if (infix == null || infix.trim().isEmpty()) {
            return "";
        }
        
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        
        // Clean input by removing spaces
        infix = infix.replaceAll("\\s+", "");
        
        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);
            
            if (Character.isDigit(c)) {
                // Process multi-digit numbers
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    number.append(infix.charAt(i++));
                }
                i--; // Adjust for loop increment
                output.append(number).append(" ");
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(operators.pop()).append(" ");
                }
                
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop(); // Discard the '('
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses in expression");
                }
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c) && operators.peek() != '(') {
                    output.append(operators.pop()).append(" ");
                }
                operators.push(c);
            }
        }
        
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses in expression");
            }
            output.append(operators.pop()).append(" ");
        }
        
        return output.toString().trim();
    }
    
    // Helper for operator precedence
    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
    
    // Create a hardcoded example tree for the given expression
    public static ArithmeticTree createExampleTree() {
        // Creating the tree for ((3+1)*3)/((9-5)+2)-((3*(7-4))+6)
        
        // Create leaf nodes (operands)
        Node three1 = new Node(3);
        Node one = new Node(1);
        Node three2 = new Node(3);
        Node nine = new Node(9);
        Node five = new Node(5);
        Node two = new Node(2);
        Node three3 = new Node(3);
        Node seven = new Node(7);
        Node four = new Node(4);
        Node six = new Node(6);
        
        // Build the expression tree bottom-up
        // (3+1)
        Node plus1 = new Node('+');
        plus1.left = three1;
        plus1.right = one;
        
        // (3+1)*3
        Node mult1 = new Node('*');
        mult1.left = plus1;
        mult1.right = three2;
        
        // (9-5)
        Node minus1 = new Node('-');
        minus1.left = nine;
        minus1.right = five;
        
        // (9-5)+2
        Node plus2 = new Node('+');
        plus2.left = minus1;
        plus2.right = two;
        
        // ((3+1)*3)/((9-5)+2)
        Node div = new Node('/');
        div.left = mult1;
        div.right = plus2;
        
        // (7-4)
        Node minus2 = new Node('-');
        minus2.left = seven;
        minus2.right = four;
        
        // 3*(7-4)
        Node mult2 = new Node('*');
        mult2.left = three3;
        mult2.right = minus2;
        
        // (3*(7-4))+6
        Node plus3 = new Node('+');
        plus3.left = mult2;
        plus3.right = six;
        
        // ((3+1)*3)/((9-5)+2)-((3*(7-4))+6)
        Node minus3 = new Node('-');
        minus3.left = div;
        minus3.right = plus3;
        
        return new ArithmeticTree(minus3);
    }
    
    // Method to print tree in prefix notation (for debugging)
    public String prefix() {
        StringBuilder sb = new StringBuilder();
        prefixHelper(root, sb);
        return sb.toString().trim();
    }
    
    private void prefixHelper(Node node, StringBuilder sb) {
        if (node == null) return;
        
        if (node.isOperator) {
            sb.append(node.operator).append(" ");
        } else {
            sb.append((int)node.operand).append(" ");
        }
        prefixHelper(node.left, sb);
        prefixHelper(node.right, sb);
    }
    
    public static void main(String[] args) {
        // Test with the hardcoded example tree
        ArithmeticTree tree = createExampleTree();
        System.out.println("Expression: ((3+1)*3)/((9-5)+2)-((3*(7-4))+6)");
        System.out.println("Postfix: " + tree.postorder());
        System.out.println("Value: " + tree.value());
        System.out.println();
        
        // Test creation from postfix
        String postfix = "3 1 + 3 * 9 5 - 2 + / 3 7 4 - * 6 + -";
        System.out.println("Creating tree from postfix: " + postfix);
        ArithmeticTree postfixTree = createFromPostfix(postfix);
        System.out.println("Resulting postfix: " + postfixTree.postorder());
        System.out.println("Value: " + postfixTree.value());
        System.out.println();
        
        // Test creation from infix
        String infix = "((3+1)*3)/((9-5)+2)-((3*(7-4))+6)";
        System.out.println("Creating tree from infix: " + infix);
        ArithmeticTree infixTree = createFromInfix(infix);
        System.out.println("Resulting postfix: " + infixTree.postorder());
        System.out.println("Value: " + infixTree.value());
        
        // Test with some additional expressions
        testExpression("5+3*2");
        testExpression("(7-2)*(3+4)/2");
        testExpression("10/(2+3)*(4-1)");
    }
    
    private static void testExpression(String infix) {
        System.out.println("\nTesting expression: " + infix);
        ArithmeticTree tree = createFromInfix(infix);
        System.out.println("Postfix: " + tree.postorder());
        System.out.println("Value: " + tree.value());
    }
}
