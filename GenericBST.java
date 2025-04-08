import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

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

public class GenericBST<K extends Comparable<K>, V> implements GenericBSTInterface<K, V> {
    
    private class Node {
        K key;          
        V val;        
        Node left, right;  

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private Node root;         

    public GenericBST() {
        root = null;
    }

    // CORE BST OPERATIONS
    @Override
    public void inorder() {
        inorder(root);
    }

    private void inorder(Node x) {
        if (x == null) return;
        inorder(x.left);
        System.out.println(x.key + ": " + x.val);
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
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node x, K key) {
        if (x == null) return null;
        
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = deleteRecursive(x.left, key);
        else if (cmp > 0) x.right = deleteRecursive(x.right, key);
        else {
            if (x.left == null && x.right == null) return null;
            
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;
            
            Node successor = min(x.right);
            x.key = successor.key;
            x.val = successor.val;
            x.right = deleteRecursive(x.right, successor.key);
        }
        return x;
    }

    // ADDITIONAL OPERATIONS
    @Override
    public void preorder() {
        preorder(root);
    }
    
    private void preorder(Node x) {
        if (x == null) return;
        System.out.println(x.key + ": " + x.val);
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
        System.out.println(x.key + ": " + x.val);
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
        return isBST(root,null, null);
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
        int leftHeight = height(x.left);
        int rightHeight = height(x.right);
        return Math.abs(leftHeight - rightHeight) <= 1 &&
               isPerfectlyBalanced(x.left) && isPerfectlyBalanced(x.right);
    }
    
    private int countNodes(Node x) {
        if (x == null) return 0;
        return 1 + countNodes(x.left) + countNodes(x.right);
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
    }
    
    // DESIGN EXERCISE OPERATIONS
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
    
    public ArrayList<K> getKeysInOrder() {
        ArrayList<K> keys = new ArrayList<>();
        collectKeysInOrder(root, keys);
        return keys;
    }
    
    private void collectKeysInOrder(Node x, ArrayList<K> keys) {
        if (x == null) return;
        collectKeysInOrder(x.left, keys);
        keys.add(x.key);
        collectKeysInOrder(x.right, keys);
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
            System.out.print(node.key + " ");
            
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
        System.out.println();
    }
    
    public static <K extends Comparable<K>, V> GenericBST<K, V> createBalancedBSTfromSortedArray(K[] keys, V[] values) {
        if (keys == null || values == null || keys.length != values.length || keys.length == 0) {
            throw new IllegalArgumentException("Invalid input arrays");
        }
        
        GenericBST<K, V> bst = new GenericBST<>();
        bst.root = createBalancedBSTfromSortedArrayHelper(keys, values, 0, keys.length - 1);
        return bst;
    }
    
    private static <K extends Comparable<K>, V> GenericBST<K, V>.Node createBalancedBSTfromSortedArrayHelper(K[] keys, V[] values, int start, int end) {
        if (start > end) return null;
        
        int mid = start + (end - start) / 2;
        GenericBST<K, V> bst = new GenericBST<>();
        GenericBST<K, V>.Node node = bst.new Node(keys[mid], values[mid]);
        
        node.left = createBalancedBSTfromSortedArrayHelper(keys, values, start, mid - 1);
        node.right = createBalancedBSTfromSortedArrayHelper(keys, values, mid + 1, end);
        
        return node;
    }
    
    public static <K extends Comparable<K>> boolean isPostorderArray(K[] array) {
        if (array == null || array.length == 0) return true;
        return isPostorderArrayHelper(array, 0, array.length - 1);
    }
    
    private static <K extends Comparable<K>> boolean isPostorderArrayHelper(K[] array, int start, int end) {
        if (start >= end) return true;
        
        K root = array[end];
        
        // Find the first element greater than root
        int i = start;
        while (i < end && array[i].compareTo(root) < 0) {
            i++;
        }
        
        // All elements from i to end-1 should be greater than root
        int j = i;
        while (j < end) {
            if (array[j].compareTo(root) <= 0) {
                return false;
            }
            j++;
        }
        
        // Recursively check left and right subtrees
        boolean leftIsValid = (i == start) || isPostorderArrayHelper(array, start, i - 1);
        boolean rightIsValid = (i == end) || isPostorderArrayHelper(array, i, end - 1);
        
        return leftIsValid && rightIsValid;
    }
    
    public static <K extends Comparable<K>, V> GenericBST<K, V> createBSTfromPostOrderArray(K[] keys, V defaultValue) {
        if (keys == null || keys.length == 0 || !isPostorderArray(keys)) {
            return null;
        }
        
        GenericBST<K, V> bst = new GenericBST<>();
        bst.root = createBSTfromPostOrderArrayHelper(keys, defaultValue, 0, keys.length - 1, bst);
        return bst;
    }
    
    private static <K extends Comparable<K>, V> GenericBST<K, V>.Node createBSTfromPostOrderArrayHelper(
            K[] keys, V defaultValue, int start, int end, GenericBST<K, V> bst) {
        if (start > end) return null;
        
        GenericBST<K, V>.Node root = bst.new Node(keys[end], defaultValue);
        
        // Find the first element greater than root
        int i = start;
        while (i < end && keys[i].compareTo(keys[end]) < 0) {
            i++;
        }
        
        // Create left and right subtrees
        root.left = createBSTfromPostOrderArrayHelper(keys, defaultValue, start, i - 1, bst);
        root.right = createBSTfromPostOrderArrayHelper(keys, defaultValue, i, end - 1, bst);
        
        return root;
    }
    
    public int depth(K key) {
        return depth(root, key, 0);
    }
    
    private int depth(Node x, K key, int currentDepth) {
        if (x == null) return -1; 
        
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return currentDepth;
        else if (cmp < 0) return depth(x.left, key, currentDepth + 1);
        else return depth(x.right, key, currentDepth + 1);
    }
    
    // Support classes for testing
    static class AnniversaryDate implements Comparable<AnniversaryDate> {
        private String name;
        private String date;

        public AnniversaryDate(String name, String date) {
            this.name = name;
            this.date = date;
        }

        @Override
        public int compareTo(AnniversaryDate other) {
            int dateComp = this.date.compareTo(other.date);
            if (dateComp != 0) return dateComp;
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + " (" + date + ")";
        }
    }

    static class PartyDescription {
        private String location;
        private int guests;
        private int duration;

        public PartyDescription(String location, int guests, int duration) {
            this.location = location;
            this.guests = guests;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "Party at " + location + " with " + guests + " guests for " + duration + " hours";
        }
    }

    static class Person implements Comparable<Person> {
        private String name;
        private String surname;
        private String ssn;

        public Person(String name, String surname, String ssn) {
            this.name = name;
            this.surname = surname;
            this.ssn = ssn;
        }

        @Override
        public int compareTo(Person other) {
            return this.ssn.compareTo(other.ssn);
        }

        @Override
        public String toString() {
            return name + " " + surname + " (SSN: " + ssn + ")";
        }
    }

    static class JobDescription {
        private String title;
        private String office;
        private double salary;

        public JobDescription(String title, String office, double salary) {
            this.title = title;
            this.office = office;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return title + " at " + office + ", $" + salary;
        }
    }
    
    static class City {
        private final String name;
        private final int size;
        private final int population;

        public City(String name, int size, int population) {
            this.name = name;
            this.size = size;
            this.population = population;
        }

        @Override
        public String toString() {
            return "City{" + "name='" + name + '\'' + ", size=" + size + ", population=" + population + '}';
        }
    }

    // Main method to test all operations
    public static void main(String[] args) {
        testStringStringBST();
        testAnniversaryDateBST();
        testPersonJobBST();
        testIntegerOperations();
        testDesignExercises();
        testNewMethods();
    }

    private static void testStringStringBST() {
        System.out.println("\n==== Testing String-String Dictionary BST ====");
        GenericBST<String, String> dictionary = new GenericBST<>();
        
        dictionary.put("algorithm", "A step-by-step procedure for solving a problem");
        dictionary.put("binary", "Relating to a system of numerical notation that has 2 as its base");
        dictionary.put("data structure", "A specialized format for organizing and storing data");
        dictionary.put("iteration", "The process of repeating a set of instructions a specified number of times");
        dictionary.put("recursion", "A method where the solution depends on solutions to smaller instances");
        
        System.out.println("Dictionary contents (inorder):");
        dictionary.inorder();
        
        System.out.println("\nTesting operations:");
        System.out.println("Contains 'binary': " + dictionary.contains("binary"));
        System.out.println("Contains 'quantum': " + dictionary.contains("quantum"));
        
        System.out.println("\nMin key: " + dictionary.min());
        System.out.println("Max key: " + dictionary.max());
        
        System.out.println("\nSuccessor of 'binary': " + dictionary.successor("binary"));
        System.out.println("Predecessor of 'iteration': " + dictionary.predecessor("iteration"));
        
        System.out.println("\nDeleting 'binary'");
        dictionary.delete("binary");
        System.out.println("Contains 'binary' after deletion: " + dictionary.contains("binary"));
        
        System.out.println("\nDictionary height: " + dictionary.height());
        System.out.println("It is a BST: " + dictionary.isBST());
        
        System.out.println("\nPrinting levels:");
        dictionary.printLevels();
    }

    private static void testAnniversaryDateBST() {
        System.out.println("\n==== Testing AnniversaryDate-PartyDescription BST ====");
        GenericBST<AnniversaryDate, PartyDescription> anniversaryBST = new GenericBST<>();
        
        anniversaryBST.put(
            new AnniversaryDate("Wedding Anniversary", "2023-06-15"),
            new PartyDescription("Grand Hotel", 150, 5)
        );
        
        anniversaryBST.put(
            new AnniversaryDate("Company Foundation", "2022-11-20"),
            new PartyDescription("Office Building", 75, 3)
        );
        
        anniversaryBST.put(
            new AnniversaryDate("Birthday", "2023-06-15"),
            new PartyDescription("Home", 30, 4)
        );
        
        anniversaryBST.put(
            new AnniversaryDate("Graduation", "2023-05-10"),
            new PartyDescription("University Hall", 200, 2)
        );
        
        System.out.println("Anniversary BST contents (inorder):");
        anniversaryBST.inorder();
        
        System.out.println("\nTesting tree traversals:");
        System.out.println("Preorder:");
        anniversaryBST.preorder();
        System.out.println("\nPostorder:");
        anniversaryBST.postorder();
        
        System.out.println("\nAnniversary BST height: " + anniversaryBST.height());
        System.out.println("It is perfectly balanced: " + anniversaryBST.isPerfectlyBalanced());
    }

    private static void testPersonJobBST() {
        System.out.println("\n==== Testing Person-JobDescription BST ====");
        GenericBST<Person, JobDescription> personBST = new GenericBST<>();
        
        personBST.put(
            new Person("John", "Smith", "123-45-6789"),
            new JobDescription("Software Engineer", "Building A", 85000.0)
        );
        
        personBST.put(
            new Person("Alice", "Johnson", "987-65-4321"),
            new JobDescription("Project Manager", "Building B", 95000.0)
        );
        
        personBST.put(
            new Person("Bob", "Williams", "456-78-9012"),
            new JobDescription("Database Administrator", "Building A", 90000.0)
        );
        
        personBST.put(
            new Person("Emily", "Davis", "111-22-3333"),
            new JobDescription("UX Designer", "Building C", 80000.0)
        );
        
        System.out.println("Person BST contents (inorder):");
        personBST.inorder();
        
        System.out.println("\nTesting iterative operations:");
        Person searchPerson = new Person("John", "Smith", "123-45-6789");
        System.out.println("Search iterative for " + searchPerson + ": " + personBST.searchIterative(searchPerson));
        
        System.out.println("Min iterative: " + personBST.minIterative());
        
        Person newPerson = new Person("David", "Brown", "555-66-7777");
        JobDescription newJob = new JobDescription("Data Scientist", "Building D", 92000.0);
        personBST.insertIterative(newPerson, newJob);
        
        System.out.println("\nAfter iterative insertion:");
        personBST.inorder();
    }

    private static void testIntegerOperations() {
        System.out.println("\n==== Testing Integer BST Operations ====");
        GenericBST<Integer, Integer> intBST = new GenericBST<>();
        
        System.out.println("Adding elements to the BST...");
        intBST.put(8, 80);
        intBST.put(3, 30);
        intBST.put(10, 100);
        intBST.put(1, 10);
        intBST.put(6, 60);
        intBST.put(14, 140);
        intBST.put(4, 40);
        intBST.put(7, 70);
        intBST.put(13, 130);
        
        System.out.println("\nInorder tree walk:");
        intBST.inorder();
        
        System.out.println("\nPreorder tree walk:");
        intBST.preorder();
        
        System.out.println("\nPostorder tree walk:");
        intBST.postorder();
        
        System.out.println("\nContains tests:");
        System.out.println("Contains 7: " + intBST.contains(7));
        System.out.println("Contains 15: " + intBST.contains(15));
        
        System.out.println("\nMin key: " + intBST.min());
        System.out.println("Max key: " + intBST.max());
        
        System.out.println("\nSuccessor tests:");
        System.out.println("Successor of 7: " + intBST.successor(7));
        System.out.println("Successor of 13: " + intBST.successor(13));
        System.out.println("Successor of 14: " + intBST.successor(14));
        
        System.out.println("\nPredecessor tests:");
        System.out.println("Predecessor of 8: " + intBST.predecessor(8));
        System.out.println("Predecessor of 4: " + intBST.predecessor(4));
        System.out.println("Predecessor of 1: " + intBST.predecessor(1));
        
        System.out.println("\nHeight of the BST: " + intBST.height());
        
        System.out.println("\nIs BST before spoiling values: " + intBST.isBST());
        
        System.out.println("\nDeleting node with key 6...");
        intBST.delete(6);
        System.out.println("BST after deletion (inorder):");
        intBST.inorder();
        
        System.out.println("\nIs perfectly balanced: " + intBST.isPerfectlyBalanced());
        
        System.out.println("\nIterative method tests:");
        System.out.println("SearchIterative for 10: " + intBST.searchIterative(10));
        System.out.println("SearchIterative for 20: " + intBST.searchIterative(20));
        System.out.println("MinIterative: " + intBST.minIterative());
        
        System.out.println("\nInserting 5 with value 50 iteratively...");
        intBST.insertIterative(5, 50);
        
        System.out.println("\nLevel order traversal:");
        intBST.printLevels();
        
        System.out.println("\nHeight:");
        System.out.println(intBST.height());
        intBST.preorder();
        System.out.println(intBST.height(8));    
        for (int i : intBST.getKeysInOrder()) {
            System.out.println("Key: " + i + ", Height: " + intBST.height(i));
        }
        
        for (int i : intBST.getKeysInOrder()) {
            System.out.println("Key: " + i + ", Depth: " + intBST.depth(i));
        }  

        System.out.println("\nSpoiling values...");
        intBST.spoilValues();
        
        System.out.println("BST after spoiling values (inorder):");
        intBST.inorder();
        
        System.out.println("Is BST after spoiling values: " + intBST.isBST());
    }

    private static void testDesignExercises() {
        System.out.println("\n==== Testing Design Exercises ====");
        GenericBST<Integer, String> bst = new GenericBST<>();
        
        bst.put(8, "Eight");
        bst.put(2, "Two");
        bst.put(15, "Fifteen");
        bst.put(1, "One");
        bst.put(5, "Five");
        bst.put(10, "Ten");
        bst.put(20, "Twenty");
        bst.put(4, "Four");
        bst.put(7, "Seven");
        bst.put(18, "Eighteen");
        bst.put(22, "Twenty-Two");
        bst.put(3, "Three");
        
        System.out.println("BST inorder:");
        bst.inorder();
        
        System.out.println("\nTesting searchClosest:");
        System.out.println("Closest to 16: " + bst.searchClosest(16));
        System.out.println("Closest to 9: " + bst.searchClosest(9));
        System.out.println("Closest to 21: " + bst.searchClosest(21));
        System.out.println("Closest to 6: " + bst.searchClosest(6));
        System.out.println("Closest to 11: " + bst.searchClosest(11));
        
        System.out.println("\nTesting checkExistTwoNodesWithSum:");
        System.out.println("Sum 12 exists: " + bst.checkExistTwoNodesWithSum(12));
        System.out.println("Sum 9 exists: " + bst.checkExistTwoNodesWithSum(9));
        System.out.println("Sum 17 exists: " + bst.checkExistTwoNodesWithSum(17));
        System.out.println("Sum 30 exists: " + bst.checkExistTwoNodesWithSum(30));
        
        System.out.println("\nTesting printPathFromTo:");
        bst.printPathFromTo(10, 18);
        bst.printPathFromTo(5, 22);
        bst.printPathFromTo(1, 7);
        bst.printPathFromTo(3, 15);
        
        System.out.println("\nTesting printPathsWithSum:");
        System.out.println("Paths with sum 15:");
        bst.printPathsWithSum(15);
        System.out.println("\nPaths with sum 22:");
        bst.printPathsWithSum(22);
        
        System.out.println("\nLevel order traversal:");
        bst.printLevels();
    }
    
    private static void testNewMethods() {
        System.out.println("\n==== Testing New BST Methods ====");
        
        // Test createBalancedBSTfromSortedArray
        System.out.println("Testing createBalancedBSTfromSortedArray:");
        Integer[] sortedKeys = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        String[] values = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        
        GenericBST<Integer, String> balancedBST = createBalancedBSTfromSortedArray(sortedKeys, values);
        System.out.println("Balanced BST inorder traversal:");
        balancedBST.inorder();
        System.out.println("Balanced BST height: " + balancedBST.height());
        System.out.println("Is perfectly balanced: " + balancedBST.isPerfectlyBalanced());
        System.out.println("Level order traversal:");
        balancedBST.printLevels();
        
        // Test isPostorderArray
        System.out.println("\nTesting isPostorderArray:");
        Integer[] validPostorder = {1, 3, 2, 5, 7, 6, 4};
        Integer[] invalidPostorder = {5, 2, 3, 1, 6, 4, 7};
        System.out.println("Is valid postorder array: " + isPostorderArray(validPostorder));
        System.out.println("Is invalid postorder array: " + isPostorderArray(invalidPostorder));
        
        // Test createBSTfromPostOrderArray
        System.out.println("\nTesting createBSTfromPostOrderArray:");
        GenericBST<Integer, String> postorderBST = createBSTfromPostOrderArray(validPostorder, "Default");
        System.out.println("BST created from postorder array, inorder traversal:");
        postorderBST.inorder();
        System.out.println("Level order traversal:");
        postorderBST.printLevels();
        
        // Test with invalid postorder array
        System.out.println("\nTesting with invalid postorder array:");
        GenericBST<Integer, String> invalidBST = createBSTfromPostOrderArray(invalidPostorder, "Default");
        System.out.println("Result (should be null): " + (invalidBST == null ? "null" : "not null"));
    }
}
