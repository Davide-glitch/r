// Base Tree interface
interface Tree<T> {
    boolean isEmpty();
}

// BST interface and implementation
interface BSTInterface<T extends Comparable<T>> extends Tree<T> {
    void insert(T data);
    boolean search(T data);
    void delete(T data);
    void inorder();
    void preorder();
    void postorder();
}

class BST<T extends Comparable<T>> implements BSTInterface<T> {
    private class Node {
        T data;
        Node left, right;
        
        Node(T data) {
            this.data = data;
            this.left = this.right = null;
        }
    }
    
    private Node root;
    
    public BST() {
        root = null;
    }
    
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }
    
    private Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        
        if (data.compareTo(root.data) < 0)
            root.left = insertRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = insertRec(root.right, data);
        
        return root;
    }
    
    @Override
    public boolean search(T data) {
        return searchRec(root, data);
    }
    
    private boolean searchRec(Node root, T data) {
        if (root == null)
            return false;
        
        if (root.data.equals(data))
            return true;
        
        if (data.compareTo(root.data) < 0)
            return searchRec(root.left, data);
        
        return searchRec(root.right, data);
    }
    
    @Override
    public void delete(T data) {
        root = deleteRec(root, data);
    }
    
    private Node deleteRec(Node root, T data) {
        if (root == null) return null;
        
        if (data.compareTo(root.data) < 0) {
            root.left = deleteRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = deleteRec(root.right, data);
        } else {
            // Node with one or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            
            // Node with two child
            root.data = minValue(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        
        return root;
    }
    
    private T minValue(Node root) {
        T minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }
    
    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " ");
            inorderRec(root.right);
        }
    }
    
    @Override
    public void preorder() {
        preorderRec(root);
        System.out.println();
    }
    
    private void preorderRec(Node root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }
    
    @Override
    public void postorder() {
        postorderRec(root);
        System.out.println();
    }
    
    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.data + " ");
        }
    }
}

// AVL Tree interface and implementation
interface AVLInterface<T extends Comparable<T>> extends BSTInterface<T> {
    int getHeight();
    void balance();
}

class AVL<T extends Comparable<T>> implements AVLInterface<T> {
    private class Node {
        T data;
        Node left, right;
        int height;
        
        Node(T data) {
            this.data = data;
            this.left = this.right = null;
            this.height = 1;
        }
    }
    
    private Node root;
    
    public AVL() {
        root = null;
    }
    
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    @Override
    public int getHeight() {
        return height(root);
    }
    
    private int height(Node node) {
        if (node == null) return 0;
        return node.height;
    }
    
    private int getBalance(Node node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }
    
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        return x;
    }
    
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        
        return y;
    }
    
    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }
    
    private Node insertRec(Node node, T data) {
        if (node == null)
            return new Node(data);
        
        if (data.compareTo(node.data) < 0)
            node.left = insertRec(node.left, data);
        else if (data.compareTo(node.data) > 0)
            node.right = insertRec(node.right, data);
        else
            return node; // Duplicate keys not allowed
        
        node.height = 1 + Math.max(height(node.left), height(node.right));
        
        int balance = getBalance(node);
        
        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0)
            return rightRotate(node);
        
        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0)
            return leftRotate(node);
        
        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        
        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        
        return node;
    }
    
    @Override
    public void delete(T data) {
        root = deleteRec(root, data);
    }
    
    private Node deleteRec(Node root, T data) {
        if (root == null)
            return root;
        
        if (data.compareTo(root.data) < 0)
            root.left = deleteRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = deleteRec(root.right, data);
        else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;
            
            root.data = minValue(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        
        if (root == null)
            return root;
        
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        
        int balance = getBalance(root);
        
        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);
        
        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        
        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);
        
        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        
        return root;
    }
    
    private T minValue(Node root) {
        T minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }
    
    @Override
    public boolean search(T data) {
        return searchRec(root, data);
    }
    
    private boolean searchRec(Node root, T data) {
        if (root == null)
            return false;
        
        if (root.data.equals(data))
            return true;
        
        if (data.compareTo(root.data) < 0)
            return searchRec(root.left, data);
        
        return searchRec(root.right, data);
    }
    
    @Override
    public void balance() {
        System.out.println("AVL tree is already balanced");
    }
    
    @Override
    public void inorder() {
        inorderRec(root);
        System.out.println();
    }
    
    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " ");
            inorderRec(root.right);
        }
    }
    
    @Override
    public void preorder() {
        preorderRec(root);
        System.out.println();
    }
    
    private void preorderRec(Node root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }
    
    @Override
    public void postorder() {
        postorderRec(root);
        System.out.println();
    }
    
    private void postorderRec(Node root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.data + " ");
        }
    }
}

// B-Tree interface and implementation
interface BTreeInterface<T extends Comparable<T>> extends Tree<T> {
    void insert(T data);
    boolean search(T data);
    void traverse();
}

class BTree<T extends Comparable<T>> implements BTreeInterface<T> {
    // B-Tree Node
    private class Node {
        int t;  // Minimum degree
        int n;  // Current number of keys
        T[] keys;
        Node[] child;
        boolean leaf;
        
        @SuppressWarnings("unchecked")
        Node(int t, boolean leaf) {
            this.t = t;
            this.leaf = leaf;
            this.keys = (T[]) new Comparable[2*t - 1];
            this.child = (Node[]) new BTree.Node[2*t];
            this.n = 0;
        }
        
        void traverse() {
            int i;
            for (i = 0; i < n; i++) {
                if (!leaf)
                    child[i].traverse();
                System.out.print(keys[i] + " ");
            }
            
            if (!leaf)
                child[i].traverse();
        }
        
        boolean search(T k) {
            int i = 0;
            while (i < n && k.compareTo(keys[i]) > 0)
                i++;
            
            if (i < n && k.compareTo(keys[i]) == 0)
                return true;
            
            if (leaf)
                return false;
            
            return child[i].search(k);
        }
        
        void insertNonFull(T k) {
            int i = n - 1;
            
            if (leaf) {
                while (i >= 0 && k.compareTo(keys[i]) < 0) {
                    keys[i + 1] = keys[i];
                    i--;
                }
                
                keys[i + 1] = k;
                n = n + 1;
            } else {
                while (i >= 0 && k.compareTo(keys[i]) < 0)
                    i--;
                
                i++;
                
                if (child[i].n == 2*t - 1) {
                    splitChild(i, child[i]);
                    
                    if (k.compareTo(keys[i]) > 0)
                        i++;
                }
                child[i].insertNonFull(k);
            }
        }
        
        void splitChild(int i, Node y) {
            Node z = new Node(y.t, y.leaf);
            z.n = t - 1;
            
            for (int j = 0; j < t - 1; j++)
                z.keys[j] = y.keys[j + t];
            
            if (!y.leaf) {
                for (int j = 0; j < t; j++)
                    z.child[j] = y.child[j + t];
            }
            
            y.n = t - 1;
            
            for (int j = n; j >= i + 1; j--)
                child[j + 1] = child[j];
            
            child[i + 1] = z;
            
            for (int j = n - 1; j >= i; j--)
                keys[j + 1] = keys[j];
            
            keys[i] = y.keys[t - 1];
            
            n = n + 1;
        }
    }
    
    private Node root;
    private int t;  // Minimum degree
    
    public BTree(int t) {
        this.root = null;
        this.t = t;
    }
    
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    @Override
    public void traverse() {
        if (root != null)
            root.traverse();
        System.out.println();
    }
    
    @Override
    public boolean search(T k) {
        if (root == null)
            return false;
        return root.search(k);
    }
    
    @Override
    public void insert(T k) {
        if (root == null) {
            root = new Node(t, true);
            root.keys[0] = k;
            root.n = 1;
        } else {
            if (root.n == 2*t - 1) {
                Node s = new Node(t, false);
                s.child[0] = root;
                s.splitChild(0, root);
                
                int i = 0;
                if (s.keys[0].compareTo(k) < 0)
                    i++;
                s.child[i].insertNonFull(k);
                
                root = s;
            } else {
                root.insertNonFull(k);
            }
        }
    }
}

// Trie Tree interface and implementation
interface TrieTreeInterface extends Tree<String> {
    void insert(String word);
    boolean search(String word);
    boolean startsWith(String prefix);
    void delete(String word);
}

class TrieTree implements TrieTreeInterface {
    private class TrieNode {
        private TrieNode[] child;
        private boolean isEndOfWord;
        
        TrieNode() {
            child = new TrieNode[26]; // For lowercase English letters
            isEndOfWord = false;
        }
    }
    
    private TrieNode root;
    
    public TrieTree() {
        root = new TrieNode();
    }
    
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < 26; i++) {
            if (root.child[i] != null)
                return false;
        }
        return true;
    }
    
    @Override
    public void insert(String word) {
        TrieNode current = root;
        
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.child[index] == null)
                current.child[index] = new TrieNode();
            
            current = current.child[index];
        }
        
        current.isEndOfWord = true;
    }
    
    @Override
    public boolean search(String word) {
        TrieNode node = searchNode(word);
        return node != null && node.isEndOfWord;
    }
    
    @Override
    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }
    
    private TrieNode searchNode(String str) {
        TrieNode current = root;
        
        for (int i = 0; i < str.length(); i++) {
            int index = str.charAt(i) - 'a';
            if (current.child[index] == null)
                return null;
            
            current = current.child[index];
        }
        
        return current;
    }
    
    @Override
    public void delete(String word) {
        delete(root, word, 0);
    }
    
    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord)
                return false;
            
            current.isEndOfWord = false;
            
            return isNodeEmpty(current);
        }
        
        int charIndex = word.charAt(index) - 'a';
        TrieNode node = current.child[charIndex];
        
        if (node == null)
            return false;
        
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1);
        
        if (shouldDeleteCurrentNode) {
            current.child[charIndex] = null;
            return isNodeEmpty(current);
        }
        
        return false;
    }
    
    private boolean isNodeEmpty(TrieNode node) {
        for (int i = 0; i < 26; i++) {
            if (node.child[i] != null)
                return false;
        }
        return !node.isEndOfWord;
    }
}

// Huffman Tree interface and implementation
interface HuffmanTreeInterface extends Tree<Character> {
    void buildTree(String text);
    String encode(String text);
    String decode(String encodedText);
}

class Huffman implements HuffmanTreeInterface {
    private class Node implements Comparable<Node> {
        Character ch;
        Integer freq;
        Node left, right;
        
        Node(Character ch, Integer freq) {
            this.ch = ch;
            this.freq = freq;
            this.left = this.right = null;
        }
        
        @Override
        public int compareTo(Node o) {
            return this.freq.compareTo(o.freq);
        }
    }
    
    private Node root;
    private java.util.HashMap<Character, String> codes;
    
    public Huffman() {
        this.root = null;
        this.codes = new java.util.HashMap<>();
    }
    
    @Override
    public boolean isEmpty() {
        return root == null;
    }
    
    @Override
    public void buildTree(String text) {
        // Count frequency of each character
        java.util.HashMap<Character, Integer> freq = new java.util.HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        
        // Create a priority queue
        java.util.PriorityQueue<Node> pq = new java.util.PriorityQueue<>();
        for (java.util.Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }
        
        // Build the Huffman tree
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            
            Node newNode = new Node(null, left.freq + right.freq);
            newNode.left = left;
            newNode.right = right;
            pq.add(newNode);
        }
        
        root = pq.poll();
        codes.clear();
        buildCodes(root, "");
    }
    
    private void buildCodes(Node root, String code) {
        if (root == null)
            return;
        
        if (root.ch != null) {
            codes.put(root.ch, code);
        }
        
        buildCodes(root.left, code + "0");
        buildCodes(root.right, code + "1");
    }
    
    @Override
    public String encode(String text) {
        if (root == null)
            buildTree(text);
        
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(codes.get(c));
        }
        return sb.toString();
    }
    
    @Override
    public String decode(String encodedText) {
        if (root == null)
            return "";
        
        StringBuilder result = new StringBuilder();
        Node current = root;
        
        for (int i = 0; i < encodedText.length(); i++) {
            current = (encodedText.charAt(i) == '0') ? current.left : current.right;
            
            if (current.ch != null) {
                result.append(current.ch);
                current = root;
            }
        }
        
        return result.toString();
    }
}

public class all {
    // Test methods
    public static void testBST() {
        System.out.println("\n===== BST TEST =====");
        BST<Integer> bst = new BST<>();
        
        bst.insert(50);
        bst.insert(30);
        bst.insert(20);
        bst.insert(40);
        bst.insert(70);
        bst.insert(60);
        bst.insert(80);
        
        System.out.println("Inorder traversal:");
        bst.inorder();
        
        System.out.println("Preorder traversal:");
        bst.preorder();
        
        System.out.println("Postorder traversal:");
        bst.postorder();
        
        System.out.println("Search 40: " + bst.search(40));
        System.out.println("Search 90: " + bst.search(90));
        
        System.out.println("Delete 20");
        bst.delete(20);
        System.out.println("Inorder traversal after deletion:");
        bst.inorder();
        
        System.out.println("Delete 30");
        bst.delete(30);
        System.out.println("Inorder traversal after deletion:");
        bst.inorder();
    }
    
    public static void testAVL() {
        System.out.println("\n===== AVL TEST =====");
        AVL<Integer> avl = new AVL<>();
        
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);  // Triggers rotation
        avl.insert(40);
        avl.insert(50);
        avl.insert(25);
        
        System.out.println("Inorder traversal (balanced tree):");
        avl.inorder();
        
        System.out.println("Height: " + avl.getHeight());
        
        System.out.println("Search 25: " + avl.search(25));
        System.out.println("Search 35: " + avl.search(35));
        
        System.out.println("Delete 10");
        avl.delete(10);
        System.out.println("Inorder traversal after deletion:");
        avl.inorder();
    }
    
    public static void testBTree() {
        System.out.println("\n===== B-Tree TEST =====");
        BTree<Integer> bTree = new BTree<>(3); // Minimum degree 3
        
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(5);
        bTree.insert(6);
        bTree.insert(12);
        bTree.insert(30);
        bTree.insert(7);
        bTree.insert(17);
        
        System.out.println("B-Tree traversal:");
        bTree.traverse();
        
        System.out.println("Search 6: " + bTree.search(6));
        System.out.println("Search 15: " + bTree.search(15));
    }
    
    public static void testTrieTree() {
        System.out.println("\n===== Trie Tree TEST =====");
        TrieTree trie = new TrieTree();
        
        trie.insert("apple");
        trie.insert("application");
        trie.insert("banana");
        trie.insert("ball");
        
        System.out.println("Search 'apple': " + trie.search("apple"));
        System.out.println("Search 'app': " + trie.search("app"));
        System.out.println("StartsWith 'app': " + trie.startsWith("app"));
        System.out.println("StartsWith 'ban': " + trie.startsWith("ban"));
        
        System.out.println("Delete 'apple'");
        trie.delete("apple");
        System.out.println("Search 'apple' after deletion: " + trie.search("apple"));
        System.out.println("Search 'application' after deletion: " + trie.search("application"));
    }
    
    public static void testHuffman() {
        System.out.println("\n===== Huffman Tree TEST =====");
        Huffman huffman = new Huffman();
        
        String text = "this is an example for huffman encoding";
        System.out.println("Original text: " + text);
        
        huffman.buildTree(text);
        String encoded = huffman.encode(text);
        System.out.println("Encoded text: " + encoded);
        
        String decoded = huffman.decode(encoded);
        System.out.println("Decoded text: " + decoded);
        System.out.println("Original and decoded are equal: " + text.equals(decoded));
    }
    
    public static void main(String[] args) {
        testBST();
        testAVL();
        testBTree();
        testTrieTree();
        testHuffman();
    }
}
