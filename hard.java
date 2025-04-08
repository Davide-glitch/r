public class hard {
    // Main method to test all implementations
    public static void main(String[] args) {
        hard h = new hard();
        h.testBST();
        h.testAVL();
        h.testBTree();
        h.testTrie();
        h.testHuffmanTree();
    }
    
    // =========================== BST Implementation ===========================
    class BSTNode {
        int key;
        BSTNode left, right;
        
        public BSTNode(int key) {
            this.key = key;
            this.left = this.right = null;
        }
    }
    
    class BST {
        BSTNode root;
        
        public BST() {
            root = null;
        }
        
        // Insert a key into the BST
        public void insert(int key) {
            root = insertRec(root, key);
        }
        
        private BSTNode insertRec(BSTNode root, int key) {
            if (root == null) {
                root = new BSTNode(key);
                return root;
            }
            
            if (key < root.key)
                root.left = insertRec(root.left, key);
            else if (key > root.key)
                root.right = insertRec(root.right, key);
            
            return root;
        }
        
        // Search for a key in the BST
        public boolean search(int key) {
            return searchRec(root, key);
        }
        
        private boolean searchRec(BSTNode root, int key) {
            if (root == null)
                return false;
            
            if (root.key == key)
                return true;
            
            if (root.key > key)
                return searchRec(root.left, key);
            
            return searchRec(root.right, key);
        }
        
        // In-order traversal
        public void inorder() {
            inorderRec(root);
            System.out.println();
        }
        
        private void inorderRec(BSTNode root) {
            if (root != null) {
                inorderRec(root.left);
                System.out.print(root.key + " ");
                inorderRec(root.right);
            }
        }
    }
    
    // =========================== AVL Tree Implementation ===========================
    class AVLNode {
        int key, height;
        AVLNode left, right;
        
        public AVLNode(int key) {
            this.key = key;
            this.height = 1; // New node is initially at height 1
            this.left = this.right = null;
        }
    }
    
    class AVLTree {
        AVLNode root;
        
        // Get height of a node
        int height(AVLNode node) {
            if (node == null)
                return 0;
            return node.height;
        }
        
        // Right rotate subtree rooted with y
        AVLNode rightRotate(AVLNode y) {
            AVLNode x = y.left;
            AVLNode T2 = x.right;
            
            // Perform rotation
            x.right = y;
            y.left = T2;
            
            // Update heights
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            
            // Return new root
            return x;
        }
        
        // Left rotate subtree rooted with x
        AVLNode leftRotate(AVLNode x) {
            AVLNode y = x.right;
            AVLNode T2 = y.left;
            
            // Perform rotation
            y.left = x;
            x.right = T2;
            
            // Update heights
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            
            // Return new root
            return y;
        }
        
        // Get balance factor of a node
        int getBalance(AVLNode node) {
            if (node == null)
                return 0;
            return height(node.left) - height(node.right);
        }
        
        // Insert a key into the tree
        public void insert(int key) {
            root = insertRec(root, key);
        }
        
        AVLNode insertRec(AVLNode node, int key) {
            // 1. Perform standard BST insertion
            if (node == null)
                return new AVLNode(key);
            
            if (key < node.key)
                node.left = insertRec(node.left, key);
            else if (key > node.key)
                node.right = insertRec(node.right, key);
            else // Duplicate keys not allowed
                return node;
            
            // 2. Update height of this ancestor node
            node.height = 1 + Math.max(height(node.left), height(node.right));
            
            // 3. Get the balance factor and balance if needed
            int balance = getBalance(node);
            
            // Left Left Case
            if (balance > 1 && key < node.left.key)
                return rightRotate(node);
            
            // Right Right Case
            if (balance < -1 && key > node.right.key)
                return leftRotate(node);
            
            // Left Right Case
            if (balance > 1 && key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            
            // Right Left Case
            if (balance < -1 && key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
            
            return node;
        }
        
        // In-order traversal
        public void inorder() {
            inorderRec(root);
            System.out.println();
        }
        
        private void inorderRec(AVLNode root) {
            if (root != null) {
                inorderRec(root.left);
                System.out.print(root.key + " ");
                inorderRec(root.right);
            }
        }
    }
    
    // =========================== B Tree Implementation ===========================
    class BTreeNode {
        int[] keys; // Array of keys
        int t; // Minimum degree (defines the range for number of keys)
        BTreeNode[] children; // Array of child pointers
        int n; // Current number of keys
        boolean leaf; // Is true when node is leaf, else false
        
        public BTreeNode(int t, boolean leaf) {
            this.t = t;
            this.leaf = leaf;
            this.keys = new int[2*t - 1];
            this.children = new BTreeNode[2*t];
            this.n = 0;
        }
        
        // Find the index of the first key greater than or equal to k
        public int findKey(int k) {
            int idx = 0;
            while (idx < n && keys[idx] < k)
                ++idx;
            return idx;
        }
        
        public void traverse() {
            // There are n keys and n+1 children
            int i;
            for (i = 0; i < n; i++) {
                // If this is not leaf, then before printing key[i],
                // traverse the subtree rooted with child C[i].
                if (!leaf)
                    children[i].traverse();
                System.out.print(keys[i] + " ");
            }
            
            // Print the subtree rooted with last child
            if (!leaf)
                children[i].traverse();
        }
        
        public BTreeNode search(int k) {
            // Find the first key greater than or equal to k
            int i = 0;
            while (i < n && k > keys[i])
                i++;
            
            // If the found key is equal to k, return this node
            if (i < n && keys[i] == k)
                return this;
            
            // If key is not found here and this is a leaf node
            if (leaf)
                return null;
            
            // Go to the appropriate child
            return children[i].search(k);
        }
        
        // A utility function to insert a new key in this node
        // The assumption is, the node must be non-full when this function is called
        void insertNonFull(int k) {
            // Initialize index as index of rightmost element
            int i = n - 1;
            
            // If this is a leaf node
            if (leaf) {
                // Find the location of new key to be inserted
                // Move all greater keys to one place ahead
                while (i >= 0 && keys[i] > k) {
                    keys[i + 1] = keys[i];
                    i--;
                }
                
                // Insert the new key at found location
                keys[i + 1] = k;
                n = n + 1;
            } else { // If this node is not leaf
                // Find the child which is going to have the new key
                while (i >= 0 && keys[i] > k)
                    i--;
                
                // See if the found child is full
                if (children[i + 1].n == 2 * t - 1) {
                    // If the child is full, then split it
                    splitChild(i + 1, children[i + 1]);
                    
                    // After split, the middle key of C[i] goes up and
                    // C[i] is split into two. See which of the two
                    // is going to have the new key
                    if (keys[i + 1] < k)
                        i++;
                }
                children[i + 1].insertNonFull(k);
            }
        }
        
        // A utility function to split the child y of this node
        // Note that y must be full when this function is called
        void splitChild(int i, BTreeNode y) {
            // Create a new node which is going to store (t-1) keys of y
            BTreeNode z = new BTreeNode(y.t, y.leaf);
            z.n = t - 1;
            
            // Copy the last (t-1) keys of y to z
            for (int j = 0; j < t - 1; j++)
                z.keys[j] = y.keys[j + t];
            
            // Copy the last t children of y to z
            if (!y.leaf) {
                for (int j = 0; j < t; j++)
                    z.children[j] = y.children[j + t];
            }
            
            // Reduce the number of keys in y
            y.n = t - 1;
            
            // Since this node is going to have a new child,
            // create space of new child
            for (int j = n; j >= i + 1; j--)
                children[j + 1] = children[j];
            
            // Link the new child to this node
            children[i + 1] = z;
            
            // A key of y will move to this node. Find location of
            // new key and move all greater keys one space ahead
            for (int j = n - 1; j >= i; j--)
                keys[j + 1] = keys[j];
            
            // Copy the middle key of y to this node
            keys[i] = y.keys[t - 1];
            
            // Increment count of keys in this node
            n = n + 1;
        }
    }
    
    class BTree {
        BTreeNode root; // Pointer to root node
        int t; // Minimum degree
        
        // Constructor
        public BTree(int t) {
            this.root = null;
            this.t = t;
        }
        
        // Function to traverse the tree
        public void traverse() {
            if (root != null)
                root.traverse();
            System.out.println();
        }
        
        // Function to search a key in this tree
        public BTreeNode search(int k) {
            return (root == null) ? null : root.search(k);
        }
        
        // Main function that inserts a new key in the B-Tree
        public void insert(int k) {
            // If tree is empty
            if (root == null) {
                // Allocate memory for root
                root = new BTreeNode(t, true);
                root.keys[0] = k; // Insert key
                root.n = 1; // Update number of keys in root
            } else { // If tree is not empty
                // If root is full, then tree grows in height
                if (root.n == 2 * t - 1) {
                    // Allocate memory for new root
                    BTreeNode s = new BTreeNode(t, false);
                    
                    // Make old root as child of new root
                    s.children[0] = root;
                    
                    // Split the old root and move 1 key to the new root
                    s.splitChild(0, root);
                    
                    // New root has two children now. Decide which of the
                    // two children is going to have new key
                    int i = 0;
                    if (s.keys[0] < k)
                        i++;
                    s.children[i].insertNonFull(k);
                    
                    // Change root
                    root = s;
                } else { // If root is not full, call insertNonFull for root
                    root.insertNonFull(k);
                }
            }
        }
    }
    
    // =========================== Trie Implementation ===========================
    class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;
        
        public TrieNode() {
            children = new TrieNode[26]; // Assuming only lowercase English letters
            isEndOfWord = false;
        }
    }
    
    class Trie {
        TrieNode root;
        
        public Trie() {
            root = new TrieNode();
        }
        
        // Insert a word into the trie
        public void insert(String word) {
            TrieNode current = root;
            
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (current.children[index] == null)
                    current.children[index] = new TrieNode();
                
                current = current.children[index];
            }
            
            current.isEndOfWord = true;
        }
        
        // Search for a word in the trie
        public boolean search(String word) {
            TrieNode current = root;
            
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (current.children[index] == null)
                    return false;
                
                current = current.children[index];
            }
            
            return current.isEndOfWord;
        }
        
        // Returns true if there is any word that starts with the given prefix
        public boolean startsWith(String prefix) {
            TrieNode current = root;
            
            for (int i = 0; i < prefix.length(); i++) {
                int index = prefix.charAt(i) - 'a';
                if (current.children[index] == null)
                    return false;
                
                current = current.children[index];
            }
            
            return true;
        }
    }
    
    // =========================== Huffman Tree Implementation ===========================
    class HuffmanNode implements Comparable<HuffmanNode> {
        int frequency;
        char data;
        HuffmanNode left, right;
        
        public HuffmanNode(char data, int frequency) {
            this.data = data;
            this.frequency = frequency;
            left = right = null;
        }
        
        @Override
        public int compareTo(HuffmanNode node) {
            return this.frequency - node.frequency;
        }
    }
    
    class HuffmanTree {
        HuffmanNode root;
        String[] codes;
        
        public HuffmanTree(char[] data, int[] freq) {
            codes = new String[256]; // For all ASCII characters
            buildHuffmanTree(data, freq);
        }
        
        // Build Huffman Tree
        private void buildHuffmanTree(char[] data, int[] freq) {
            int n = data.length;
            
            // Create a priority queue (min-heap)
            java.util.PriorityQueue<HuffmanNode> pq = new java.util.PriorityQueue<>();
            
            // Create a leaf node for each character and add it to the priority queue
            for (int i = 0; i < n; i++) {
                pq.add(new HuffmanNode(data[i], freq[i]));
            }
            
            // Build the Huffman tree by taking the two nodes with lowest frequency
            // and combining them to form a new internal node
            while (pq.size() > 1) {
                HuffmanNode left = pq.poll();
                HuffmanNode right = pq.poll();
                
                // Create a new internal node with these two as children
                // and with frequency equal to sum of the two nodes' frequencies
                HuffmanNode internalNode = new HuffmanNode('\0', left.frequency + right.frequency);
                internalNode.left = left;
                internalNode.right = right;
                
                // Add this node to the priority queue
                pq.add(internalNode);
            }
            
            // The root is the only node left in the queue
            root = pq.poll();
            
            // Generate Huffman codes
            generateCodes(root, "");
        }
        
        // Generate Huffman codes for each character
        private void generateCodes(HuffmanNode node, String code) {
            if (node == null)
                return;
            
            // If this is a leaf node, store the code
            if (node.data != '\0') {
                codes[node.data] = code;
            }
            
            // Traverse left with code + "0"
            generateCodes(node.left, code + "0");
            
            // Traverse right with code + "1"
            generateCodes(node.right, code + "1");
        }
        
        // Encode a string using the Huffman codes
        public String encode(String text) {
            StringBuilder encodedString = new StringBuilder();
            
            for (char c : text.toCharArray()) {
                encodedString.append(codes[c]);
            }
            
            return encodedString.toString();
        }
        
        // Decode a Huffman-encoded string
        public String decode(String encodedText) {
            StringBuilder decodedString = new StringBuilder();
            HuffmanNode current = root;
            
            for (char bit : encodedText.toCharArray()) {
                if (bit == '0') {
                    current = current.left;
                } else {
                    current = current.right;
                }
                
                // If this is a leaf node
                if (current.left == null && current.right == null) {
                    decodedString.append(current.data);
                    current = root;
                }
            }
            
            return decodedString.toString();
        }
        
        // Print Huffman codes
        public void printCodes() {
            System.out.println("Character - Huffman Code");
            for (int i = 0; i < 256; i++) {
                if (codes[i] != null) {
                    System.out.println((char)i + " - " + codes[i]);
                }
            }
        }
    }
    
    // =========================== Test Methods ===========================
    public void testBST() {
        System.out.println("\n----- Testing Binary Search Tree -----");
        BST bst = new BST();
        
        // Insert elements
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);
        
        System.out.println("In-order traversal of BST:");
        bst.inorder(); // Should print: 20 30 40 50 60 70 80
        
        System.out.println("Searching for 40: " + bst.search(40)); // Should print: true
        System.out.println("Searching for 100: " + bst.search(100)); // Should print: false
    }
    
    public void testAVL() {
        System.out.println("\n----- Testing AVL Tree -----");
        AVLTree avlTree = new AVLTree();
        
        // Insert elements
        avlTree.insert(10);
        avlTree.insert(20);
        avlTree.insert(30); // This will cause rotations
        avlTree.insert(40);
        avlTree.insert(50); // This will cause rotations
        avlTree.insert(25);
        
        System.out.println("In-order traversal of AVL tree:");
        avlTree.inorder(); // Should print: 10 20 25 30 40 50
    }
    
    public void testBTree() {
        System.out.println("\n----- Testing B-Tree -----");
        BTree bTree = new BTree(3); // A B-Tree with minimum degree 3
        
        // Insert elements
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(5);
        bTree.insert(6);
        bTree.insert(12);
        bTree.insert(30);
        bTree.insert(7);
        bTree.insert(17);
        
        System.out.println("Traversal of the B-Tree:");
        bTree.traverse(); // Should print keys in sorted order
        
        System.out.println("Searching for 6: " + (bTree.search(6) != null)); // Should print: true
        System.out.println("Searching for 15: " + (bTree.search(15) != null)); // Should print: false
    }
    
    public void testTrie() {
        System.out.println("\n----- Testing Trie -----");
        Trie trie = new Trie();
        
        // Insert words
        trie.insert("hello");
        trie.insert("help");
        trie.insert("world");
        trie.insert("word");
        
        System.out.println("Search for 'hello': " + trie.search("hello")); // Should print: true
        System.out.println("Search for 'hell': " + trie.search("hell")); // Should print: false
        System.out.println("Prefix 'hel' exists: " + trie.startsWith("hel")); // Should print: true
        System.out.println("Prefix 'wor' exists: " + trie.startsWith("wor")); // Should print: true
        System.out.println("Prefix 'abc' exists: " + trie.startsWith("abc")); // Should print: false
    }
    
    public void testHuffmanTree() {
        System.out.println("\n----- Testing Huffman Tree -----");
        
        // Sample data
        char[] data = {'a', 'b', 'c', 'd', 'e', 'f'};
        int[] freq = {5, 9, 12, 13, 16, 45};
        
        HuffmanTree huffmanTree = new HuffmanTree(data, freq);
        
        System.out.println("Huffman Codes:");
        huffmanTree.printCodes();
        
        String text = "abacdebf";
        String encoded = huffmanTree.encode(text);
        System.out.println("\nOriginal text: " + text);
        System.out.println("Encoded text: " + encoded);
        
        String decoded = huffmanTree.decode(encoded);
        System.out.println("Decoded text: " + decoded);
    }
}