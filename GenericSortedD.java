import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericSortedD<K extends Comparable<K>, V> implements MySortedMap<K, V> {
    private class Node {
        K key;
        V value;
        Node left, right;
        int height;
        
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }
    
    private Node root;
    
    // Helper methods for AVL tree
    private int height(Node x) {
        if (x == null) return 0;
        return x.height;
    }
    
    private int balanceFactor(Node x) {
        if (x == null) return 0;
        return height(x.left) - height(x.right);
    }
    
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        // Perform rotation
        x.right = y;
        y.left = T2;
        
        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        return x;
    }
    
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        
        // Perform rotation
        y.left = x;
        x.right = T2;
        
        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        
        return y;
    }
    
    // Already implemented methods
    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = put(root, key, value);
    }
    
    private Node put(Node x, K key, V value) {
        if (x == null) return new Node(key, value);
        
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, value);
        else if (cmp > 0) x.right = put(x.right, key, value);
        else x.value = value;
        
        // Update height
        x.height = 1 + Math.max(height(x.left), height(x.right));
        
        // Balance the tree
        int balance = balanceFactor(x);
        
        // Left heavy
        if (balance > 1) {
            if (key.compareTo(x.left.key) < 0) {
                // Left-Left case
                return rotateRight(x);
            } else {
                // Left-Right case
                x.left = rotateLeft(x.left);
                return rotateRight(x);
            }
        }
        
        // Right heavy
        if (balance < -1) {
            if (key.compareTo(x.right.key) > 0) {
                // Right-Right case
                return rotateLeft(x);
            } else {
                // Right-Left case
                x.right = rotateRight(x.right);
                return rotateLeft(x);
            }
        }
        
        return x;
    }
    
    @Override
    public Iterable<K> getKeys() {
        List<K> keys = new ArrayList<>();
        inorder(root, keys);
        return keys;
    }
    
    private void inorder(Node x, List<K> keys) {
        if (x == null) return;
        inorder(x.left, keys);
        keys.add(x.key);
        inorder(x.right, keys);
    }
    
    // Methods to be implemented
    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return get(key) != null;
    }
    
    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        Node x = get(root, key);
        return x == null ? null : x.value;
    }
    
    private Node get(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x;
    }
    
    @Override
    public void remove(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = remove(root, key);
    }
    
    private Node remove(Node x, K key) {
        if (x == null) return null;
        
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key);
        } else if (cmp > 0) {
            x.right = remove(x.right, key);
        } else {
            // Node with only one child or no child
            if (x.left == null) return x.right;
            else if (x.right == null) return x.left;
            
            // Node with two children: Get the inorder successor
            Node temp = min(x.right);
            x.key = temp.key;
            x.value = temp.value;
            x.right = remove(x.right, temp.key);
        }
        
        // Update height
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        // Check balance
        int balance = balanceFactor(x);
        
        // Left heavy
        if (balance > 1) {
            if (balanceFactor(x.left) >= 0) {
                // Left-Left case
                return rotateRight(x);
            } else {
                // Left-Right case
                x.left = rotateLeft(x.left);
                return rotateRight(x);
            }
        }
        
        // Right heavy
        if (balance < -1) {
            if (balanceFactor(x.right) <= 0) {
                // Right-Right case
                return rotateLeft(x);
            } else {
                // Right-Left case
                x.right = rotateRight(x.right);
                return rotateLeft(x);
            }
        }
        
        return x;
    }
    
    private Node min(Node x) {
        if (x == null) return null;
        if (x.left == null) return x;
        return min(x.left);
    }
    
    @Override
    public Iterable<Map.Entry<K, V>> getEntries() {
        List<Map.Entry<K, V>> entries = new ArrayList<>();
        inorderEntries(root, entries);
        return entries;
    }
    
    private void inorderEntries(Node x, List<Map.Entry<K, V>> entries) {
        if (x == null) return;
        inorderEntries(x.left, entries);
        entries.add(new AbstractMap.SimpleEntry<>(x.key, x.value));
        inorderEntries(x.right, entries);
    }
    
    @Override
    public K firstKey() {
        if (root == null) return null;
        Node min = min(root);
        return min != null ? min.key : null;
    }
    
    @Override
    public K lastKey() {
        if (root == null) return null;
        Node max = max(root);
        return max != null ? max.key : null;
    }
    
    private Node max(Node x) {
        if (x == null) return null;
        if (x.right == null) return x;
        return max(x.right);
    }
    
    // Test client
    public static void main(String[] args) {
        // Test our AVL Map implementation
        GenericSortedD<Integer, String> avlMap = new GenericSortedD<>();
        System.out.println("Testing GenericSortedD (AVL-based Map)");
        System.out.println("-----------------------------------");
        
        System.out.println("Inserting key-value pairs...");
        avlMap.put(5, "Five");
        avlMap.put(3, "Three");
        avlMap.put(7, "Seven");
        avlMap.put(2, "Two");
        avlMap.put(4, "Four");
        avlMap.put(6, "Six");
        avlMap.put(8, "Eight");
        
        // Test getting keys
        System.out.println("\nAVL Map Keys:");
        for (Integer key : avlMap.getKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();
        
        // Test getting entries
        System.out.println("\nAVL Map Entries:");
        for (Map.Entry<Integer, String> entry : avlMap.getEntries()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        
        // Test containsKey and get
        System.out.println("\nContains key 4: " + avlMap.containsKey(4));
        System.out.println("Contains key 9: " + avlMap.containsKey(9));
        System.out.println("Value for key 4: " + avlMap.get(4));
        System.out.println("Value for key 9: " + avlMap.get(9));
        
        // Test firstKey and lastKey
        System.out.println("\nFirst key: " + avlMap.firstKey());
        System.out.println("Last key: " + avlMap.lastKey());
        
        // Test remove
        System.out.println("\nRemoving key 4");
        avlMap.remove(4);
        System.out.println("Keys after removal:");
        for (Integer key : avlMap.getKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();
        
        // Test removing root
        System.out.println("\nRemoving root key 5");
        avlMap.remove(5);
        System.out.println("Keys after removal:");
        for (Integer key : avlMap.getKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();
        
        // Check balance after multiple operations
        System.out.println("\nAdding more elements to test balancing");
        avlMap.put(1, "One");
        avlMap.put(9, "Nine");
        avlMap.put(0, "Zero");
        avlMap.put(10, "Ten");
        
        System.out.println("Final keys in order:");
        for (Integer key : avlMap.getKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
}

/**
 * Interface for Sorted Map operations
 */
interface MySortedMap<K extends Comparable<K>, V> {
    void put(K key, V value);
    Iterable<K> getKeys();
    boolean containsKey(K key);
    V get(K key);
    void remove(K key);
    Iterable<Map.Entry<K, V>> getEntries();
    K firstKey();
    K lastKey();
}
