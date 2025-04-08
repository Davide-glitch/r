public class hard {
    // Main method to test all implementations
    public static void main(String[] args) {
        hard h = new hard();
        //h.testBST();
        //h.testAVL();
        //h.testBTree();
        h.testTrie();
        //h.testHuffmanTree();
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
        
        // Level order traversal of B-Tree
        public void displayLevels() {
            if (root == null) {
                System.out.println("Empty tree");
                return;
            }
            
            java.util.Queue<Object[]> queue = new java.util.LinkedList<>();
            System.out.println("B-Tree displayed by levels:");
            queue.add(new Object[]{root, 0});
            
            int currentLevel = -1;
            
            while (!queue.isEmpty()) {
                Object[] pair = queue.poll();
                BTreeNode node = (BTreeNode) pair[0];
                int level = (int) pair[1];
                
                if (level > currentLevel) {
                    System.out.println();
                    System.out.print("Level " + level + ": ");
                    currentLevel = level;
                }
                
                // Print the node's keys
                System.out.print("[ ");
                for (int i = 0; i < node.n; i++) {
                    System.out.print(node.keys[i]);
                    if (i < node.n - 1) System.out.print(" ");
                }
                System.out.print(" ] ");
                
                // Add children to queue
                if (!node.leaf) {
                    for (int i = 0; i <= node.n; i++) {
                        queue.add(new Object[]{node.children[i], level + 1});
                    }
                }
            }
            System.out.println();
        }
    }
    
    // =========================== Trie Implementation ===========================
    class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;
        Integer value; // Add value storage to the TrieNode
        
        public TrieNode() {
            children = new TrieNode[26]; // Assuming only lowercase English letters
            isEndOfWord = false;
            value = null;
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
        
        // Print all words in the trie with their values
        public void printAllWords() {
            System.out.println("Words in the Trie:");
            printAllWords(root, new StringBuilder());
        }
        
        private void printAllWords(TrieNode node, StringBuilder prefix) {
            if (node == null) return;
            
            if (node.isEndOfWord) {
                String output = node.value == null 
                    ? prefix.toString() 
                    : "(" + prefix.toString() + ", " + node.value + ")";
                System.out.println(output);
            }
            
            for (char c = 'a'; c <= 'z'; c++) {
                int index = c - 'a';
                if (node.children[index] != null) {
                    prefix.append(c);
                    printAllWords(node.children[index], prefix);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
        }
        
        // Insert a word with a value
        public void insert(String word, Integer value) {
            TrieNode current = root;
            
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'a';
                if (current.children[index] == null)
                    current.children[index] = new TrieNode();
                
                current = current.children[index];
            }
            
            current.isEndOfWord = true;
            current.value = value;
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
    // Create a hardcoded BST structure
    public BST createHardcodedBST() {
        BST bst = new BST();
        
        // Create root node with key 50
        bst.root = new BSTNode(50);
        
        // Create level 2 nodes
        bst.root.left = new BSTNode(30);
        bst.root.right = new BSTNode(70);
        
        // Create level 3 nodes
        bst.root.left.left = new BSTNode(20);
        bst.root.left.right = new BSTNode(40);
        bst.root.right.left = new BSTNode(60);
        bst.root.right.right = new BSTNode(80);
        
        return bst;
    }
    
    // Create a hardcoded AVL Tree structure
    public AVLTree createHardcodedAVL() {
        AVLTree avl = new AVLTree();
        
        // Create a balanced tree with elements 10, 20, 25, 30, 40, 50
        avl.root = new AVLNode(30);
        avl.root.height = 3;
        
        avl.root.left = new AVLNode(20);
        avl.root.left.height = 2;
        
        avl.root.right = new AVLNode(40);
        avl.root.right.height = 2;
        
        avl.root.left.left = new AVLNode(10);
        avl.root.left.left.height = 1;
        
        avl.root.left.right = new AVLNode(25);
        avl.root.left.right.height = 1;
        
        avl.root.right.right = new AVLNode(50);
        avl.root.right.right.height = 1;
        
        return avl;
    }
    
    // Create a more complex hardcoded B-Tree structure
    public BTree createHardcodedBTree() {
        BTree btree = new BTree(3);
        
        // Create root
        btree.root = new BTreeNode(3, false);
        btree.root.keys[0] = 5;
        btree.root.keys[1] = 12;
        btree.root.keys[2] = 16;
        btree.root.keys[3] = 23;
        btree.root.keys[4] = 30;
        btree.root.n = 5;
        
        // Create level 1 children
        BTreeNode child1 = new BTreeNode(3, true);
        child1.keys[0] = 1;
        child1.keys[1] = 2;
        child1.keys[2] = 3;
        child1.n = 3;
        
        BTreeNode child2 = new BTreeNode(3, true);
        child2.keys[0] = 6;
        child2.keys[1] = 7;
        child2.keys[2] = 9;
        child2.keys[3] = 10;
        child2.keys[4] = 11;
        child2.n = 5;
        
        BTreeNode child3 = new BTreeNode(3, true);
        child3.keys[0] = 13;
        child3.keys[1] = 14;
        child3.n = 2;
        
        BTreeNode child4 = new BTreeNode(3, true);
        child4.keys[0] = 18;
        child4.keys[1] = 19;
        child4.keys[2] = 20;
        child4.n = 3;
        
        BTreeNode child5 = new BTreeNode(3, true);
        child5.keys[0] = 24;
        child5.keys[1] = 26;
        child5.n = 2;
        
        BTreeNode child6 = new BTreeNode(3, true);
        child6.keys[0] = 32;
        child6.keys[1] = 36;
        child6.n = 2;
        
        // Connect children to root
        btree.root.children[0] = child1;
        btree.root.children[1] = child2;
        btree.root.children[2] = child3;
        btree.root.children[3] = child4;
        btree.root.children[4] = child5;
        btree.root.children[5] = child6;
        
        return btree;
    }
    
    // Create a hardcoded Trie with values
    public Trie createHardcodedTrie() {
        Trie trie = new Trie();
        
        // Build trie directly based on the TrieTree example
        trie.root = new TrieNode();
        
        // Add 't' branch
        TrieNode tNode = new TrieNode();
        trie.root.children['t' - 'a'] = tNode;
        
        // Add "to" -> 7
        TrieNode oNode = new TrieNode();
        tNode.children['o' - 'a'] = oNode;
        oNode.isEndOfWord = true;
        oNode.value = 7;
        
        // Add 'te' branch
        TrieNode eNode = new TrieNode();
        tNode.children['e' - 'a'] = eNode;
        
        // Add "tea" -> 3
        TrieNode aNode = new TrieNode();
        eNode.children['a' - 'a'] = aNode;
        aNode.isEndOfWord = true;
        aNode.value = 3;
        
        // Add "ted" -> 4
        TrieNode dNode = new TrieNode();
        eNode.children['d' - 'a'] = dNode;
        dNode.isEndOfWord = true;
        dNode.value = 4;
        
        // Add "ten" -> 12
        TrieNode nNode = new TrieNode();
        eNode.children['n' - 'a'] = nNode;
        nNode.isEndOfWord = true;
        nNode.value = 12;
        
        // Add 'i' branch 
        TrieNode iNode = new TrieNode();
        trie.root.children['i' - 'a'] = iNode;
        iNode.isEndOfWord = true;
        iNode.value = 11;
        
        // Add "in" -> 5
        TrieNode inNode = new TrieNode();
        iNode.children['n' - 'a'] = inNode;
        inNode.isEndOfWord = true;
        inNode.value = 5;
        
        // Add "inn" -> 9
        TrieNode innNode = new TrieNode();
        inNode.children['n' - 'a'] = innNode;
        innNode.isEndOfWord = true;
        innNode.value = 9;
        
        // Also add the original examples
        TrieNode hNode = new TrieNode();
        trie.root.children['h' - 'a'] = hNode;
        
        // Add "hello"
        TrieNode elNode = new TrieNode();
        hNode.children['e' - 'a'] = elNode;
        
        TrieNode llNode = new TrieNode();
        elNode.children['l' - 'a'] = llNode;
        
        TrieNode loNode = new TrieNode();
        llNode.children['l' - 'a'] = loNode;
        
        TrieNode oNode2 = new TrieNode();
        loNode.children['o' - 'a'] = oNode2;
        oNode2.isEndOfWord = true;
        
        // Add "help" reusing existing nodes
        TrieNode pNode = new TrieNode();
        llNode.children['p' - 'a'] = pNode;
        pNode.isEndOfWord = true;
        
        return trie;
    }
    
    // Create a hardcoded Huffman Tree structure
    public HuffmanTree createHardcodedHuffmanTree() {
        char[] data = {'a', 'b', 'c', 'd', 'e', 'f'};
        int[] freq = {5, 9, 12, 13, 16, 45};
        
        // Create a shell HuffmanTree object
        HuffmanTree tree = new HuffmanTree(data, freq);
        
        // Create leaf nodes
        HuffmanNode nodeA = new HuffmanNode('a', 5);
        HuffmanNode nodeB = new HuffmanNode('b', 9);
        HuffmanNode nodeC = new HuffmanNode('c', 12);
        HuffmanNode nodeD = new HuffmanNode('d', 13);
        HuffmanNode nodeE = new HuffmanNode('e', 16);
        HuffmanNode nodeF = new HuffmanNode('f', 45);
        
        // Create internal nodes
        HuffmanNode nodeAB = new HuffmanNode('\0', 14);
        nodeAB.left = nodeA;
        nodeAB.right = nodeB;
        
        HuffmanNode nodeCD = new HuffmanNode('\0', 25);
        nodeCD.left = nodeC;
        nodeCD.right = nodeD;
        
        HuffmanNode nodeABCD = new HuffmanNode('\0', 39);
        nodeABCD.left = nodeAB;
        nodeABCD.right = nodeCD;
        
        HuffmanNode nodeABCDE = new HuffmanNode('\0', 55);
        nodeABCDE.left = nodeABCD;
        nodeABCDE.right = nodeE;
        
        // Root node
        HuffmanNode root = new HuffmanNode('\0', 100);
        root.left = nodeABCDE;
        root.right = nodeF;
        
        // Set the root and regenerate codes
        tree.root = root;
        tree.generateCodes(tree.root, "");
        
        return tree;
    }
    
    public void testBST() {
        System.out.println("\n----- Testing Binary Search Tree -----");
        BST bst = createHardcodedBST();
        
        System.out.println("In-order traversal of BST:");
        bst.inorder(); // Should print: 20 30 40 50 60 70 80
        
        System.out.println("Searching for 40: " + bst.search(40)); // Should print: true
        System.out.println("Searching for 100: " + bst.search(100)); // Should print: false
    }
    
    public void testAVL() {
        System.out.println("\n----- Testing AVL Tree -----");
        AVLTree avlTree = createHardcodedAVL();
        
        System.out.println("In-order traversal of AVL tree:");
        avlTree.inorder(); // Should print: 10 20 25 30 40 50
    }
    
    public void testBTree() {
        System.out.println("\n----- Testing B-Tree -----");
        BTree bTree = createHardcodedBTree();
        
        System.out.println("Traversal of the B-Tree:");
        bTree.traverse(); // Should print keys in sorted order
        
        System.out.println("\nB-Tree displayed by levels:");
        bTree.displayLevels();
        
        System.out.println("Searching for 6: " + (bTree.search(6) != null)); // Should print: true
        System.out.println("Searching for 15: " + (bTree.search(15) != null)); // Should print: false
    }
    
    public void testTrie() {
        System.out.println("\n----- Testing Trie -----");
        Trie trie = createHardcodedTrie();
        
        System.out.println("Search for 'hello': " + trie.search("hello")); // Should print: true
        System.out.println("Search for 'hell': " + trie.search("hell")); // Should print: false
        System.out.println("Prefix 'hel' exists: " + trie.startsWith("hel")); // Should print: true
        System.out.println("Prefix 'wor' exists: " + trie.startsWith("wor")); // Should print: false
        System.out.println("Prefix 'te' exists: " + trie.startsWith("te")); // Should print: true
        
        // Print all words with values in the trie
        trie.printAllWords();
    }
    
    public void testHuffmanTree() {
        System.out.println("\n----- Testing Huffman Tree -----");
        
        HuffmanTree huffmanTree = createHardcodedHuffmanTree();
        
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