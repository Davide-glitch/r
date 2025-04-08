import java.util.Scanner;

public class sq {
    public static void main(String[] args) {
        // Test standard implementations
        testArrayStack();
        testLinkedListQueue();
        
        // Test crazy implementations
        testCrazyQueue();
        testCrazyStack();
        
        // Test stack clients
        testStackClients();
    }
    
    // =========================== INTERFACES ===========================
    
    /**
     * Stack interface that defines the standard stack operations
     */
    interface MyStack<T> {
        void push(T item);    // Add an item to the top of the stack
        T pop();              // Remove and return the top item
        T peek();             // Return the top item without removing it
        boolean isEmpty();    // Check if stack is empty
        int size();           // Return number of items in the stack
    }
    
    /**
     * Queue interface that defines the standard queue operations
     */
    interface MyQueue<T> {
        void enqueue(T item); // Add an item to the end of the queue
        T dequeue();          // Remove and return the front item
        T peek();             // Return the front item without removing it
        boolean isEmpty();    // Check if queue is empty
        int size();           // Return number of items in the queue
    }
    
    // =========================== STACK IMPLEMENTATIONS ===========================
    
    /**
     * Stack implementation using a resizable array
     */
    static class ArrayStack<T> implements MyStack<T> {
        private T[] items;    // Array to store stack elements
        private int n;        // Size of the stack
        
        @SuppressWarnings("unchecked")
        public ArrayStack() {
            // Start with a small array and grow as needed
            items = (T[]) new Object[2];
            n = 0;
        }
        
        @Override
        public void push(T item) {
            // Double size of array if necessary
            if (n == items.length) {
                resize(2 * items.length);
            }
            items[n++] = item;
        }
        
        @Override
        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            T item = items[--n];
            items[n] = null;  // Avoid loitering
            
            // Shrink array if it's too large
            if (n > 0 && n == items.length / 4) {
                resize(items.length / 2);
            }
            return item;
        }
        
        @Override
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return items[n - 1];
        }
        
        @Override
        public boolean isEmpty() {
            return n == 0;
        }
        
        @Override
        public int size() {
            return n;
        }
        
        @SuppressWarnings("unchecked")
        private void resize(int capacity) {
            T[] temp = (T[]) new Object[capacity];
            for (int i = 0; i < n; i++) {
                temp[i] = items[i];
            }
            items = temp;
        }
    }
    
    /**
     * Stack implementation using two queues
     */
    static class CrazyStack<T> implements MyStack<T> {
        private MyQueue<T> primary;
        private MyQueue<T> secondary;
        
        public CrazyStack() {
            // Using linked list queues for implementation
            primary = new LinkedListQueue<>();
            secondary = new LinkedListQueue<>();
        }
        
        @Override
        public void push(T item) {
            // Add new item to primary queue
            primary.enqueue(item);
        }
        
        @Override
        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            // Move all but the last element to secondary queue
            while (primary.size() > 1) {
                secondary.enqueue(primary.dequeue());
            }
            
            // Last element is the one we want to pop
            T item = primary.dequeue();
            
            // Swap queues
            MyQueue<T> temp = primary;
            primary = secondary;
            secondary = temp;
            
            return item;
        }
        
        @Override
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            // Move all but the last element to secondary queue
            while (primary.size() > 1) {
                secondary.enqueue(primary.dequeue());
            }
            
            // Get the last element (top of stack)
            T item = primary.peek();
            
            // Move it to secondary queue
            secondary.enqueue(primary.dequeue());
            
            // Swap queues
            MyQueue<T> temp = primary;
            primary = secondary;
            secondary = temp;
            
            return item;
        }
        
        @Override
        public boolean isEmpty() {
            return primary.isEmpty();
        }
        
        @Override
        public int size() {
            return primary.size();
        }
    }
    
    // =========================== QUEUE IMPLEMENTATIONS ===========================
    
    /**
     * Queue implementation using a linked list
     */
    static class LinkedListQueue<T> implements MyQueue<T> {
        private Node first;  // Front of queue
        private Node last;   // End of queue
        private int n;       // Size of queue
        
        // Node class for linked list
        private class Node {
            T item;
            Node next;
        }
        
        public LinkedListQueue() {
            first = null;
            last = null;
            n = 0;
        }
        
        @Override
        public void enqueue(T item) {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            
            if (isEmpty()) {
                first = last;
            } else {
                oldLast.next = last;
            }
            n++;
        }
        
        @Override
        public T dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            T item = first.item;
            first = first.next;
            n--;
            
            if (isEmpty()) {
                last = null;
            }
            
            return item;
        }
        
        @Override
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            return first.item;
        }
        
        @Override
        public boolean isEmpty() {
            return first == null;
        }
        
        @Override
        public int size() {
            return n;
        }
    }
    
    /**
     * Queue implementation using two stacks
     */
    static class CrazyQueue<T> implements MyQueue<T> {
        private MyStack<T> inbox;   // For enqueue operations
        private MyStack<T> outbox;  // For dequeue operations
        
        public CrazyQueue() {
            inbox = new ArrayStack<>();
            outbox = new ArrayStack<>();
        }
        
        @Override
        public void enqueue(T item) {
            // Always add to inbox stack
            inbox.push(item);
        }
        
        @Override
        public T dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            // If outbox is empty, move everything from inbox
            if (outbox.isEmpty()) {
                while (!inbox.isEmpty()) {
                    outbox.push(inbox.pop());
                }
            }
            
            return outbox.pop();
        }
        
        @Override
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            // If outbox is empty, move everything from inbox
            if (outbox.isEmpty()) {
                while (!inbox.isEmpty()) {
                    outbox.push(inbox.pop());
                }
            }
            
            return outbox.peek();
        }
        
        @Override
        public boolean isEmpty() {
            return inbox.isEmpty() && outbox.isEmpty();
        }
        
        @Override
        public int size() {
            return inbox.size() + outbox.size();
        }
    }
    
    // =========================== STACK CLIENT IMPLEMENTATIONS ===========================
    
    static class StackClient1 {
        // Reads n integers and prints them in reverse order
        public static void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter number of integers: ");
            int n = scanner.nextInt();
            
            MyStack<Integer> stack = new ArrayStack<>();
            
            System.out.println("Enter " + n + " integers:");
            for (int i = 0; i < n; i++) {
                stack.push(scanner.nextInt());
            }
            
            System.out.println("\nIntegers in reverse order:");
            while (!stack.isEmpty()) {
                System.out.print(stack.pop() + " ");
            }
            System.out.println();
        }
    }
    
    static class StackClient2 {
        // Reads n pairs of integers and strings, prints them in reverse order
        public static void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter number of pairs: ");
            int n = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            MyStack<Integer> intStack = new ArrayStack<>();
            MyStack<String> stringStack = new ArrayStack<>();
            
            System.out.println("Enter " + n + " pairs (integer and string on each line):");
            for (int i = 0; i < n; i++) {
                int num = scanner.nextInt();
                String str = scanner.next();
                
                intStack.push(num);
                stringStack.push(str);
            }
            
            System.out.println("\nIntegers in reverse order:");
            while (!intStack.isEmpty()) {
                System.out.print(intStack.pop() + " ");
            }
            
            System.out.println("\nStrings in reverse order:");
            while (!stringStack.isEmpty()) {
                System.out.print(stringStack.pop() + " ");
            }
            System.out.println();
        }
    }
    
    static class StackClient3 {
        // Moves bottom element to top
        public static void run() {
            MyStack<Integer> stack = new ArrayStack<>();
            // Fill stack with sample data
            int[] data = {7, 3, 1, 9, 6, 8};
            System.out.println("Original stack (bottom to top): [7, 3, 1, 9, 6, 8]");
            
            for (int value : data) {
                stack.push(value);
            }
            
            // Transform the stack
            moveBottomToTop(stack);
            
            // Print result
            System.out.println("Transformed stack (bottom to top): [3, 1, 9, 6, 8, 7]");
            
            // Verify by popping all elements
            System.out.print("Popping stack: ");
            while (!stack.isEmpty()) {
                System.out.print(stack.pop() + " ");
            }
            System.out.println();
        }
        
        // Helper method to move bottom element to top
        private static <T> void moveBottomToTop(MyStack<T> stack) {
            if (stack.isEmpty() || stack.size() == 1) {
                return;
            }
            
            // Use helper stack to reverse elements
            MyStack<T> helper = new ArrayStack<>();
            
            // Pop all but the bottom element
            while (stack.size() > 1) {
                helper.push(stack.pop());
            }
            
            // Get the bottom element
            T bottom = stack.pop();
            
            // Move everything back
            while (!helper.isEmpty()) {
                stack.push(helper.pop());
            }
            
            // Put the bottom on top
            stack.push(bottom);
        }
    }
    
    static class StackClient4 {
        // Generic stack transformer
        public static void run() {
            // Test with Integer stack
            System.out.println("Testing with Integer stack:");
            MyStack<Integer> intStack = new ArrayStack<>();
            for (int i = 1; i <= 5; i++) {
                intStack.push(i);
            }
            System.out.println("Before transformation (bottom to top): [1, 2, 3, 4, 5]");
            StackTransformer.moveBottomToTop(intStack);
            System.out.print("After transformation (popping): ");
            while (!intStack.isEmpty()) {
                System.out.print(intStack.pop() + " ");
            }
            System.out.println("\n");
            
            // Test with String stack
            System.out.println("Testing with String stack:");
            MyStack<String> stringStack = new ArrayStack<>();
            stringStack.push("apple");
            stringStack.push("banana");
            stringStack.push("cherry");
            stringStack.push("date");
            System.out.println("Before transformation (bottom to top): [apple, banana, cherry, date]");
            StackTransformer.moveBottomToTop(stringStack);
            System.out.print("After transformation (popping): ");
            while (!stringStack.isEmpty()) {
                System.out.print(stringStack.pop() + " ");
            }
            System.out.println();
        }
        
        // Generic stack transformer class
        static class StackTransformer {
            public static <T> void moveBottomToTop(MyStack<T> stack) {
                if (stack.isEmpty() || stack.size() == 1) {
                    return;
                }
                
                MyStack<T> helper = new ArrayStack<>();
                
                // Pop all but the bottom element
                while (stack.size() > 1) {
                    helper.push(stack.pop());
                }
                
                // Get the bottom element
                T bottom = stack.pop();
                
                // Move everything back
                while (!helper.isEmpty()) {
                    stack.push(helper.pop());
                }
                
                // Put the bottom on top
                stack.push(bottom);
            }
        }
    }
    
    static class StackClient5 {
        // Moves minimum value to top
        public static void run() {
            MyStack<Integer> stack = new ArrayStack<>();
            // Fill stack with sample data
            int[] data = {7, 3, 1, 9, 6, 8};
            
            System.out.println("Original stack (bottom to top): [7, 3, 1, 9, 6, 8]");
            for (int value : data) {
                stack.push(value);
            }
            
            // Transform the stack
            moveMinToTop(stack);
            
            System.out.println("Transformed stack (bottom to top): [7, 3, 9, 6, 8, 1]");
            
            // Verify by popping all elements
            System.out.print("Popping stack: ");
            while (!stack.isEmpty()) {
                System.out.print(stack.pop() + " ");
            }
            System.out.println();
        }
        
        // Helper method to move minimum value to top
        private static void moveMinToTop(MyStack<Integer> stack) {
            if (stack.isEmpty() || stack.size() == 1) {
                return;
            }
            
            MyStack<Integer> helper = new ArrayStack<>();
            int min = Integer.MAX_VALUE;
            
            // Find minimum and move everything to helper stack
            while (!stack.isEmpty()) {
                int current = stack.pop();
                min = Math.min(min, current);
                helper.push(current);
            }
            
            // Move everything back to original stack except the minimum
            boolean minFound = false;
            while (!helper.isEmpty()) {
                int current = helper.pop();
                if (current == min && !minFound) {
                    minFound = true; // Skip the first occurrence of min
                } else {
                    stack.push(current);
                }
            }
            
            // Push min on top
            stack.push(min);
        }
    }
    
    static class StackClient6 {
        // Checks for balanced parentheses
        public static void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a mathematical expression: ");
            String expression = scanner.nextLine();
            
            String result = checkBalancedParentheses(expression);
            System.out.println(result);
        }
        
        private static String checkBalancedParentheses(String expression) {
            MyStack<Character> stack = new ArrayStack<>();
            
            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                
                // If opening delimiter, push onto stack
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                }
                // If closing delimiter, check for match
                else if (c == ')' || c == ']' || c == '}') {
                    if (stack.isEmpty()) {
                        return "Error: Missing left delimiter for " + c;
                    }
                    
                    char top = stack.pop();
                    
                    // Check if delimiters match
                    if ((c == ')' && top != '(') ||
                        (c == ']' && top != '[') ||
                        (c == '}' && top != '{')) {
                        return "Error: Mismatched delimiters: expected closing for " + top + " but found " + c;
                    }
                }
            }
            
            // Check if any delimiters are left unclosed
            if (!stack.isEmpty()) {
                return "Error: Missing right delimiter for " + stack.peek();
            }
            
            return "Parentheses are balanced!";
        }
    }
    
    // =========================== TESTING METHODS ===========================
    
    private static void testArrayStack() {
        System.out.println("===== Testing Array-based Stack =====");
        // Test with integers
        MyStack<Integer> intStack = new ArrayStack<>();
        intStack.push(10);
        intStack.push(20);
        intStack.push(30);
        
        System.out.println("Integer stack size: " + intStack.size());
        System.out.println("Popping from integer stack: " + intStack.pop());
        System.out.println("Peeking at integer stack: " + intStack.peek());
        System.out.println("Integer stack is empty: " + intStack.isEmpty());
        System.out.println("Popping remaining elements: " + intStack.pop() + ", " + intStack.pop());
        System.out.println("Integer stack is empty: " + intStack.isEmpty());
        
        // Test with strings
        MyStack<String> stringStack = new ArrayStack<>();
        stringStack.push("apple");
        stringStack.push("banana");
        stringStack.push("cherry");
        
        System.out.println("\nString stack size: " + stringStack.size());
        System.out.println("Popping from string stack: " + stringStack.pop());
        System.out.println("Peeking at string stack: " + stringStack.peek());
        
        // Test with Double objects
        MyStack<Double> doubleStack = new ArrayStack<>();
        doubleStack.push(3.14);
        doubleStack.push(2.71);
        doubleStack.push(1.618);
        
        System.out.println("\nDouble stack size: " + doubleStack.size());
        System.out.println("Popping from double stack: " + doubleStack.pop());
        System.out.println("Peeking at double stack: " + doubleStack.peek());
        
        System.out.println();
    }
    
    private static void testLinkedListQueue() {
        System.out.println("===== Testing Linked List Queue =====");
        // Test with integers
        MyQueue<Integer> intQueue = new LinkedListQueue<>();
        intQueue.enqueue(10);
        intQueue.enqueue(20);
        intQueue.enqueue(30);
        
        System.out.println("Integer queue size: " + intQueue.size());
        System.out.println("Dequeuing from integer queue: " + intQueue.dequeue());
        System.out.println("Peeking at integer queue: " + intQueue.peek());
        System.out.println("Integer queue is empty: " + intQueue.isEmpty());
        
        // Test with strings
        MyQueue<String> stringQueue = new LinkedListQueue<>();
        stringQueue.enqueue("apple");
        stringQueue.enqueue("banana");
        stringQueue.enqueue("cherry");
        
        System.out.println("\nString queue size: " + stringQueue.size());
        System.out.println("Dequeuing from string queue: " + stringQueue.dequeue());
        System.out.println("Peeking at string queue: " + stringQueue.peek());
        
        // Test with Character objects
        MyQueue<Character> charQueue = new LinkedListQueue<>();
        charQueue.enqueue('A');
        charQueue.enqueue('B');
        charQueue.enqueue('C');
        
        System.out.println("\nCharacter queue size: " + charQueue.size());
        System.out.println("Dequeuing from character queue: " + charQueue.dequeue());
        System.out.println("Peeking at character queue: " + charQueue.peek());
        
        System.out.println();
    }
    
    private static void testCrazyQueue() {
        System.out.println("===== Testing Crazy Queue (Using Two Stacks) =====");
        // Test with integers
        MyQueue<Integer> crazyQueue = new CrazyQueue<>();
        crazyQueue.enqueue(10);
        crazyQueue.enqueue(20);
        crazyQueue.enqueue(30);
        
        System.out.println("Queue size: " + crazyQueue.size());
        System.out.println("Dequeuing: " + crazyQueue.dequeue());
        System.out.println("Peeking: " + crazyQueue.peek());
        System.out.println("Queue is empty: " + crazyQueue.isEmpty());
        
        // Add more items and check FIFO order
        crazyQueue.enqueue(40);
        crazyQueue.enqueue(50);
        
        System.out.println("\nDequeuing all (should be in FIFO order): " + 
                         crazyQueue.dequeue() + ", " + 
                         crazyQueue.dequeue() + ", " + 
                         crazyQueue.dequeue());
        System.out.println("Queue is empty: " + crazyQueue.isEmpty());
        
        System.out.println();
    }
    
    private static void testCrazyStack() {
        System.out.println("===== Testing Crazy Stack (Using Two Queues) =====");
        // Test with integers
        MyStack<Integer> crazyStack = new CrazyStack<>();
        crazyStack.push(10);
        crazyStack.push(20);
        crazyStack.push(30);
        
        System.out.println("Stack size: " + crazyStack.size());
        System.out.println("Popping: " + crazyStack.pop());
        System.out.println("Peeking: " + crazyStack.peek());
        System.out.println("Stack is empty: " + crazyStack.isEmpty());
        
        // Add more items and check LIFO order
        crazyStack.push(40);
        crazyStack.push(50);
        
        System.out.println("\nPopping all (should be in LIFO order): " + 
                         crazyStack.pop() + ", " + 
                         crazyStack.pop() + ", " + 
                         crazyStack.pop());
        System.out.println("Stack is empty: " + crazyStack.isEmpty());
        
        System.out.println();
    }
    
    private static void testStackClients() {
        System.out.println("===== Testing Stack Clients =====");
        System.out.println("\nStack Client 3 (Move bottom element to top):");
        StackClient3.run();
        
        System.out.println("\nStack Client 4 (Generic transformer):");
        StackClient4.run();
        
        System.out.println("\nStack Client 5 (Move minimum to top):");
        StackClient5.run();
        
        // Uncomment to run interactive clients
        /*
        System.out.println("\nStack Client 1 (Reverse integers):");
        StackClient1.run();
        
        System.out.println("\nStack Client 2 (Reverse integers and strings):");
        StackClient2.run();
        
        System.out.println("\nStack Client 6 (Check balanced parentheses):");
        StackClient6.run();
        */
    }
}