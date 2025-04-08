import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

public class GenericRotBST<K extends Comparable<K>, V> implements GenericBSTInterface<K, V> {
    
    private class Node {
        K key;          
        V val;        
        Node left, right;
        int size;  // Added size attribute to track subtree size

        Node(K key, V val) {
            this.key = key;
            this.val = val;
            this.size = 1; // Initialize to 1 (just this node)
        }
    }

    private Node root;         

    public GenericRotBST() {
        root = null;
    }
    
    // Helper to get size of a node (handles null case)
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }
    
    // Update size of a node based on its children's sizes
    private void updateSize(Node x) {
        if (x != null) {
            x.size = 1 + size(x.left) + size(x.right);
        }
    }
    
    // Rotation operations
    
    /**
     * Performs a left rotation on the node with the given key
     */
    public void rotateLeft(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = rotateLeft(root, key);
    }
    
    private Node rotateLeft(Node h, K key) {
        if (h == null) return null;
        
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = rotateLeft(h.left, key);
        } else if (cmp > 0) {
            h.right = rotateLeft(h.right, key);
        } else {
            // We found the node to rotate
            if (h.right == null) return h; // Cannot rotate
            
            Node x = h.right;
            h.right = x.left;
            x.left = h;
            
            // Update sizes
            updateSize(h);
            updateSize(x);
            
            return x;
        }
        
        // Update size after recursive call
        updateSize(h);
        return h;
    }
    
    /**
     * Performs a right rotation on the node with the given key
     */
    public void rotateRight(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = rotateRight(root, key);
    }
    
    private Node rotateRight(Node h, K key) {
        if (h == null) return null;
        
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = rotateRight(h.left, key);
        } else if (cmp > 0) {
            h.right = rotateRight(h.right, key);
        } else {
            // We found the node to rotate
            if (h.left == null) return h; // Cannot rotate
            
            Node x = h.left;
            h.left = x.right;
            x.right = h;
            
            // Update sizes
            updateSize(h);
            updateSize(x);
            
            return x;
        }
        
        // Update size after recursive call
        updateSize(h);
        return h;
    }
    
    // Core BST operations with size maintenance
    
    @Override
    public void inorder() {
        inorder(root);
    }

    private void inorder(Node x) {
        if (x == null) return;
        inorder(x.left);
        System.out.println(x.key + ": " + x.val + " (size: " + x.size + ")");
        inorder(x.right);
    }

    @Override
    public boolean contains(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }
    
    private V get(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    @Override
    public void put(K key, V val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(root, key, val);
    }

    private Node put(Node x, K key, V val) {
        if (x == null) return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        
        // Update size of this node
        updateSize(x);
        return x;
    }

    @Override
    public K min() {
        if (root == null) throw new NoSuchElementException("called min() with empty BST");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    @Override
    public K max() {
        if (root == null) throw new NoSuchElementException("called max() with empty BST");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

    @Override
    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = delete(root, key);
    }

    private Node delete(Node x, K key) {
        if (x == null) return null;
        
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            // Node to be deleted found
            
            // If only one child or no children
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;
            
            // Node with two children
            Node successor = min(x.right);
            x.key = successor.key;
            x.val = successor.val;
            x.right = deleteMin(x.right);
        }
        
        // Update size
        updateSize(x);
        return x;
    }
    
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        updateSize(x);
        return x;
    }

    // Additional operations from the interface
    @Override
    public void preorder() {
        preorder(root);
    }
    
    private void preorder(Node x) {
        if (x == null) return;
        System.out.println(x.key + ": " + x.val + " (size: " + x.size + ")");
        preorder(x.left);
        preorder(x.right);
    }
    
    @Override
    public void postorder() {
        postorder(root);
    }
    
    private void postorder(Node x) {
        if (x == null) return;
        postorder(x.left);
        postorder(x.right);
        System.out.println(x.key + ": " + x.val + " (size: " + x.size + ")");
    }
    
    @Override
    public int height() {
        return height(root);
    }
    
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    @Override
    public int height(K key) {
        Node node = findNode(root, key);
        if (node == null) return -1; 
        return height(node);
    }
    
    private Node findNode(Node x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return findNode(x.left, key);
        else if (cmp > 0) return findNode(x.right, key);
        else return x; 
    }
    
    @Override
    public boolean isBST() {
        return isBST(root, null, null);
    }
    
    private boolean isBST(Node x, K min, K max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }
    
    @Override
    public void spoilValues() {
        Random random = new Random();
        spoilValues(root, random);
    }
    
    private void spoilValues(Node x, Random random) {
        if (x == null) return;
        
        if (x.val instanceof Integer) {
            x.val = (V) Integer.valueOf(random.nextInt(100));
        } else if (x.val instanceof String) {
            x.val = (V) ("Random" + random.nextInt(100));
        }
        
        spoilValues(x.left, random);
        spoilValues(x.right, random);
    }
    
    @Override
    public K successor(K key) {
        Node x = root;
        Node successor = null;
        
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                successor = x;
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                if (x.right != null) {
                    return min(x.right).key;
                }
                break;
            }
        }
        
        if (x == null) return null;
        return successor != null ? successor.key : null;
    }
    
    @Override
    public K predecessor(K key) {
        Node x = root;
        Node predecessor = null;
        
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp > 0) {
                predecessor = x;
                x = x.right;
            } else if (cmp < 0) {
                x = x.left;
            } else {
                if (x.left != null) {
                    return max(x.left).key;
                }
                break;
            }
        }
        
        if (x == null) return null;
        return predecessor != null ? predecessor.key : null;
    }
    
    @Override
    public boolean isPerfectlyBalanced() {
        return isPerfectlyBalanced(root);
    }
    
    private boolean isPerfectlyBalanced(Node x) {
        if (x == null) return true;
        int leftSize = size(x.left);
        int rightSize = size(x.right);
        return Math.abs(leftSize - rightSize) <= 1 &&
               isPerfectlyBalanced(x.left) && isPerfectlyBalanced(x.right);
    }
    
    @Override
    public V searchIterative(K key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.val;
        }
        return null;
    }
    
    @Override
    public K minIterative() {
        if (root == null) throw new NoSuchElementException("called minIterative() with empty BST");
        Node x = root;
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }
    
    @Override
    public void insertIterative(K key, V val) {
        if (key == null) throw new IllegalArgumentException("calls insertIterative() with a null key");
        
        Node newNode = new Node(key, val);
        if (root == null) {
            root = newNode;
            return;
        }
        
        Node x = root;
        Node parent = null;
        
        while (x != null) {
            parent = x;
            updateSize(x); // Update size while traversing down
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                x.val = val;
                return;
            }
        }
        
        int cmp = key.compareTo(parent.key);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        
        // Update sizes back up the path
        x = parent;
        while (x != null) {
            updateSize(x);
            // Need to find the path back to the root
            x = findParent(root, x.key);
        }
    }
    
    private Node findParent(Node root, K key) {
        if (root == null) return null;
        if ((root.left != null && key.compareTo(root.left.key) == 0) || 
            (root.right != null && key.compareTo(root.right.key) == 0)) {
            return root;
        }
        
        int cmp = key.compareTo(root.key);
        if (cmp < 0) return findParent(root.left, key);
        else return findParent(root.right, key);
    }
    
    // Design exercise operations
    @Override
    public K searchClosest(K key) {
        if (root == null) return null;
        
        Node closest = null;
        Node current = root;
        
        while (current != null) {
            if (closest == null || 
                Math.abs(distanceBetween(key, current.key)) < Math.abs(distanceBetween(key, closest.key)) ||
                (Math.abs(distanceBetween(key, current.key)) == Math.abs(distanceBetween(key, closest.key)) && key.compareTo(current.key) == 0)) {
                closest = current;
            }
            
            int cmp = key.compareTo(current.key);
            if (cmp == 0) break; 
            else if (cmp < 0) current = current.left;
            else current = current.right;
        }
        
        return closest != null ? closest.key : null;
    }
    
    private double distanceBetween(K key1, K key2) {
        if (key1 instanceof Number && key2 instanceof Number) {
            return Math.abs(((Number)key1).doubleValue() - ((Number)key2).doubleValue());
        }
        
        return Math.abs((double)key1.compareTo(key2));
    }
    
    @Override
    public boolean checkExistTwoNodesWithSum(int sum) {
        if (root == null) return false;
        ArrayList<K> values = new ArrayList<>();
        inorderCollect(root, values);
        
        int i = 0;
        int j = values.size() - 1;
        
        while (i < j) {
            if (!(values.get(i) instanceof Number) || !(values.get(j) instanceof Number)) {
                return false;
            }
            
            int currentSum = ((Number)values.get(i)).intValue() + ((Number)values.get(j)).intValue();
            if (currentSum == sum) return true;
            else if (currentSum < sum) i++;
            else j--;
        }
        
        return false;
    }
    
    private void inorderCollect(Node x, ArrayList<K> values) {
        if (x == null) return;
        inorderCollect(x.left, values);
        values.add(x.key);
        inorderCollect(x.right, values);
    }
    
    @Override
    public void printPathFromTo(K key1, K key2) {
        if (!contains(key1) || !contains(key2)) {
            System.out.println("One or both keys not found in the BST");
            return;
        }
        
        List<K> path = findPath(root, key1, key2);
        System.out.print("Path from " + key1 + " to " + key2 + ": ");
        for (K key : path) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
    
    private List<K> findPath(Node root, K key1, K key2) {
        List<K> path = new ArrayList<>();
        
        // Find lowest common ancestor
        Node lca = findLCA(root, key1, key2);
        
        // Find path from LCA to key1
        List<K> pathToKey1 = new ArrayList<>();
        findPathToNode(lca, key1, pathToKey1);
        Collections.reverse(pathToKey1);
        
        // Find path from LCA to key2
        List<K> pathToKey2 = new ArrayList<>();
        findPathToNode(lca, key2, pathToKey2);
        pathToKey2.remove(0); // Remove LCA as it's already included
        
        // Combine paths
        path.addAll(pathToKey1);
        path.addAll(pathToKey2);
        
        return path;
    }
    
    private boolean findPathToNode(Node node, K key, List<K> path) {
        if (node == null) return false;
        
        path.add(node.key);
        
        if (node.key.equals(key)) return true;
        
        int cmp = key.compareTo(node.key);
        boolean found;
        if (cmp < 0) {
            found = findPathToNode(node.left, key, path);
        } else {
            found = findPathToNode(node.right, key, path);
        }
        
        if (!found) {
            path.remove(path.size() - 1);
        }
        
        return found;
    }
    
    private Node findLCA(Node node, K key1, K key2) {
        if (node == null) return null;
        
        if (key1.compareTo(node.key) < 0 && key2.compareTo(node.key) < 0)
            return findLCA(node.left, key1, key2);
            
        if (key1.compareTo(node.key) > 0 && key2.compareTo(node.key) > 0)
            return findLCA(node.right, key1, key2);
            
        return node;
    }
    
    @Override
    public void printPathsWithSum(int sum) {
        List<K> path = new ArrayList<>();
        printPathsWithSum(root, sum, path);
    }
    
    private void printPathsWithSum(Node node, int sum, List<K> path) {
        if (node == null) return;
        
        path.add(node.key);
        
        int currentSum = 0;
        for (int i = path.size() - 1; i >= 0; i--) {
            K key = path.get(i);
            int nodeValue;
            
            if (key instanceof Number) {
                nodeValue = ((Number)key).intValue();
            } else {
                try {
                    nodeValue = Integer.parseInt(key.toString());
                } catch (NumberFormatException e) {
                    continue; 
                }
            }
            
            currentSum += nodeValue;
            
            if (currentSum == sum) {
                System.out.print("Path with sum " + sum + ": ");
                for (int j = i; j < path.size(); j++) {
                    System.out.print(path.get(j) + " ");
                }
                System.out.println();
            }
        }

        printPathsWithSum(node.left, sum, path);
        printPathsWithSum(node.right, sum, path);
        
        path.remove(path.size() - 1);
    }
    
    @Override
    public void printLevels() {
        if (root == null) return;
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        
        System.out.print("Level order: ");
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.key + "(" + node.size + ") ");
            
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        System.out.println();
    }
    
    // Get the size of a subtree
    public int getSize(K key) {
        Node node = findNode(root, key);
        if (node == null) return 0;
        return node.size;
    }
    
    // Get the total size of the tree
    public int size() {
        return size(root);
    }
    
    // A simple test method to demonstrate rotations and size maintenance
    public static void main(String[] args) {
        GenericRotBST<Integer, String> bst = new GenericRotBST<>();
        
        // Insert elements
        bst.put(5, "Five");
        bst.put(3, "Three");
        bst.put(7, "Seven");
        bst.put(2, "Two");
        bst.put(4, "Four");
        bst.put(6, "Six");
        bst.put(8, "Eight");
        
        System.out.println("Initial BST:");
        bst.inorder();
        System.out.println("Total size: " + bst.size());
        bst.printLevels();
        
        // Test rotations
        System.out.println("\nAfter rotating left at 5:");
        bst.rotateLeft(5);
        bst.inorder();
        System.out.println("Total size: " + bst.size());
        bst.printLevels();
        
        System.out.println("\nAfter rotating right at 7:");
        bst.rotateRight(7);
        bst.inorder();
        System.out.println("Total size: " + bst.size());
        bst.printLevels();
        
        // Test delete
        System.out.println("\nAfter deleting 3:");
        bst.delete(3);
        bst.inorder();
        System.out.println("Total size: " + bst.size());
        bst.printLevels();
        
        // Test size of subtrees
        System.out.println("\nSize of subtree rooted at 5: " + bst.getSize(5));
        System.out.println("Size of subtree rooted at 7: " + bst.getSize(7));
    }
}

/**
 * Interface for Generic Binary Search Tree operations
 */
interface GenericBSTInterface<K extends Comparable<K>, V> {
    
    // Core BST operations
    void inorder();
    boolean contains(K key);
    V get(K key);
    void put(K key, V val);
    K min();
    K max();
    void delete(K key);
    
    // Additional operations
    void preorder();
    void postorder();
    int height();
    int height(K key);
    boolean isBST();
    void spoilValues();
    K successor(K key);
    K predecessor(K key);
    boolean isPerfectlyBalanced();
    V searchIterative(K key);
    K minIterative();
    void insertIterative(K key, V val);
    
    // Design exercises
    K searchClosest(K key);
    boolean checkExistTwoNodesWithSum(int sum);
    void printPathFromTo(K key1, K key2);
    void printPathsWithSum(int sum);
    void printLevels();
}
