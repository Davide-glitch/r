import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

class GenericBST<K extends Comparable<K>, V> {

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

    public void inorder() {
        inorder(root);
    }

    private void inorder(Node x) {
        if (x == null) return;
        inorder(x.left);
        System.out.println(x.key + ": " + x.val);
        inorder(x.right);
    }

    public boolean contains(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return get(key) != null;
    }

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

    public K min() {
        if (root == null) throw new NoSuchElementException("called min() with empty BST");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public K max() {
        if (root == null) throw new NoSuchElementException("called max() with empty BST");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        return max(x.right);
    }

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

    public void preorder() {
        preorder(root);
    }
    
    private void preorder(Node x) {
        if (x == null) return;
        System.out.println(x.key + ": " + x.val);
        preorder(x.left);
        preorder(x.right);
    }
    
    public void postorder() {
        postorder(root);
    }
    
    private void postorder(Node x) {
        if (x == null) return;
        postorder(x.left);
        postorder(x.right);
        System.out.println(x.key + ": " + x.val);
    }
    
    public int height() {
        return height(root);
    }
    
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

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
    
    public boolean isBST() {
        return isBST(root, null, null);
    }
    
    private boolean isBST(Node x, K min, K max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }
        
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
    
    public K minIterative() {
        if (root == null) throw new NoSuchElementException("called minIterative() with empty BST");
        Node x = root;
        while (x.left != null) {
            x = x.left;
        }
        return x.key;
    }
    
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
    
    private void inorderCollect(Node x, ArrayList<K> values) {
        if (x == null) return;
        inorderCollect(x.left, values);
        values.add(x.key);
        inorderCollect(x.right, values);
    }
    
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
    
    private boolean printPathDown(Node node, K key, boolean printSelf) {
        if (node == null) return false;
        
        if (node.key.equals(key)) {
            if (printSelf) System.out.print(node.key + " ");
            return true;
        }
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (printSelf) System.out.print(node.key + " ");
            return printPathDown(node.left, key, printSelf);
        } else {
            if (printSelf) System.out.print(node.key + " ");
            return printPathDown(node.right, key, printSelf);
        }
    }
    

    public boolean checkExistTwoNodesWithSum(int sum) {
        if (root == null) return false;
        ArrayList<K> values = new ArrayList<>();
        inorderCollect(root, values);
        
        int i = 0;
        int j = values.size() - 1;
        
        while (i < j) {
            int currentSum = ((Number)values.get(i)).intValue() + ((Number)values.get(j)).intValue();
            if (currentSum == sum) return true;
            else if (currentSum < sum) i++;
            else j--;
        }
        
        return false;
    }

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
            int nodeValue = 0;
            
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
}

class AnniversaryDate implements Comparable<AnniversaryDate> {
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

class PartyDescription {
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

class Person implements Comparable<Person> {
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

class JobDescription {
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

public class GenericBSTClient {
    public static void main(String[] args) {
        //Comment the line below to run the tests if you want to test the methods individually 
        //testStringStringBST();
        //testAnniversaryDateBST();
        //testPersonJobBST();
        testIntegerOperations();
        //testDesignExercises();
        //testCityBST();
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
        
        System.out.println("BST after iterative insertion (inorder):");
        intBST.inorder();

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
        System.out.println("Closest to 19: " + bst.searchClosest(19));
        
        System.out.println("\nTesting checkExistTwoNodesWithSum:");
        System.out.println("Sum 12 exists: " + bst.checkExistTwoNodesWithSum(12));
        System.out.println("Sum 9 exists: " + bst.checkExistTwoNodesWithSum(9));
        System.out.println("Sum 17 exists: " + bst.checkExistTwoNodesWithSum(17));
        System.out.println("Sum 25 exists: " + bst.checkExistTwoNodesWithSum(25));
        System.out.println("Sum 30 exists: " + bst.checkExistTwoNodesWithSum(30));
        System.out.println("Sum 100 exists: " + bst.checkExistTwoNodesWithSum(100));
        
        System.out.println("\nTesting printPathFromTo:");
        System.out.print("Path from 10 to 18: ");
        bst.printPathFromTo(10, 18);
        System.out.print("Path from 5 to 22: ");
        bst.printPathFromTo(5, 22);
        System.out.print("Path from 1 to 7: ");
        bst.printPathFromTo(1, 7);
        System.out.print("Path from 3 to 15: ");
        bst.printPathFromTo(3, 15);
        System.out.print("Path from 4 to 10: ");
        bst.printPathFromTo(4, 10);
        System.out.print("Path from 2 to 20: ");
        bst.printPathFromTo(2, 20);
        
        
        GenericBST<Integer, String> sumBST = new GenericBST<>();
        sumBST.put(8, "Eight");
        sumBST.put(2, "Two");
        sumBST.put(5, "Five");
        sumBST.put(4, "Four");
        sumBST.put(7, "Seven");
        sumBST.put(3, "Three");
        sumBST.put(10, "Ten");
        sumBST.put(1, "One");
        sumBST.put(12, "Twelve");
        
        
        System.out.println("\nTesting printPathsWithSum on a different BST:");
        System.out.println("Paths with sum 22:");
        sumBST.printPathsWithSum(22);
        System.out.println("\nPaths with sum 15:");
        sumBST.printPathsWithSum(15);
        System.out.println("\nPaths with sum 19:");
        sumBST.printPathsWithSum(19);
        System.out.println("\nPaths with sum 30:");
        sumBST.printPathsWithSum(30);
        System.out.println("\nPaths with sum 11:");
        sumBST.printPathsWithSum(11);
        
        
        System.out.println("\nPrint levels for the original BST:");
        bst.printLevels();
        
        System.out.println("\nPrint levels for the sumBST:");
        sumBST.printLevels();
        
        
        System.out.println("\n==== Additional Testing with Different BST Structures ====");
        
        
        GenericBST<Integer, String> smallBST = new GenericBST<>();
        smallBST.put(4, "Four");
        smallBST.put(2, "Two");
        smallBST.put(6, "Six");
        smallBST.put(1, "One");
        smallBST.put(3, "Three");
        smallBST.put(5, "Five");
        smallBST.put(7, "Seven");
        
        System.out.println("\nSmall BST level order:");
        smallBST.printLevels();
        
        System.out.println("\nTesting searchClosest on small BST:");
        System.out.println("Closest to 0: " + smallBST.searchClosest(0));
        System.out.println("Closest to 8: " + smallBST.searchClosest(8));
        
        System.out.println("\nTesting checkExistTwoNodesWithSum on small BST:");
        System.out.println("Sum 6 exists: " + smallBST.checkExistTwoNodesWithSum(6));
        System.out.println("Sum 10 exists: " + smallBST.checkExistTwoNodesWithSum(10));
        
        
        GenericBST<Integer, String> complexBST = new GenericBST<>();
        complexBST.put(1, "One");
        complexBST.put(3, "Three");
        complexBST.put(5, "Five");
        complexBST.put(7, "Seven");
        complexBST.put(9, "Nine");
        
        System.out.println("\nRight-complex BST level order:");
        complexBST.printLevels();
        
        System.out.println("\nPaths with sum 8 in complex BST:");
        complexBST.printPathsWithSum(8);
        
        System.out.print("\nPath from 1 to 9 in complex BST: ");
        complexBST.printPathFromTo(1, 9);
    }

    private static void testCityBST() {
        System.out.println("\n==== Comprehensive Test for GenericBST ====");
        
        GenericBST<String, City> cityBST = new GenericBST<>();
        
        cityBST.put("Timisoara", new City("Timisoara", 230, 300000));
        cityBST.put("Arad", new City("Arad", 180, 210000));
        cityBST.put("Brasov", new City("Brasov", 200, 280000));
        cityBST.put("Cluj", new City("Cluj", 210, 320000));
        cityBST.put("Oradea", new City("Oradea", 190, 230000));
        cityBST.put("Iasi", new City("Iasi", 170, 290000));
        cityBST.put("Sibiu", new City("Sibiu", 160, 170000));
        
        System.out.println("City BST contents (inorder):");
        cityBST.inorder();
        
        System.out.println("\nPreorder traversal:");
        cityBST.preorder();
        
        System.out.println("\nPostorder traversal:");
        cityBST.postorder();
        
        System.out.println("\nGet operations:");
        System.out.println("Get 'Cluj': " + cityBST.get("Cluj"));
        System.out.println("Get 'Bucuresti': " + cityBST.get("Bucuresti"));
        
        System.out.println("\nContains operations:");
        System.out.println("Contains 'Arad': " + cityBST.contains("Arad"));
        System.out.println("Contains 'Bucuresti': " + cityBST.contains("Bucuresti"));
        
        System.out.println("\nMin key: " + cityBST.min());
        System.out.println("Max key: " + cityBST.max());
        
        System.out.println("\nSuccessor operations:");
        System.out.println("Successor of 'Arad': " + cityBST.successor("Arad"));
        System.out.println("Successor of 'Oradea': " + cityBST.successor("Oradea"));
        System.out.println("Successor of 'Timisoara': " + cityBST.successor("Timisoara"));
        
        System.out.println("\nPredecessor operations:");
        System.out.println("Predecessor of 'Cluj': " + cityBST.predecessor("Cluj"));
        System.out.println("Predecessor of 'Brasov': " + cityBST.predecessor("Brasov"));
        System.out.println("Predecessor of 'Arad': " + cityBST.predecessor("Arad"));
        
        System.out.println("\nHeight operations:");
        System.out.println("Overall BST height: " + cityBST.height());
        System.out.println("Height of 'Brasov' subtree: " + cityBST.height("Brasov"));
        System.out.println("Height of 'Cluj' subtree: " + cityBST.height("Cluj"));
        System.out.println("Height of 'Timisoara' subtree: " + cityBST.height("Timisoara"));
        
        System.out.println("\nDepth operations:");
        System.out.println("Depth of 'Arad': " + cityBST.depth("Arad"));
        System.out.println("Depth of 'Brasov': " + cityBST.depth("Brasov"));
        System.out.println("Depth of 'Sibiu': " + cityBST.depth("Sibiu"));
        System.out.println("Depth of 'Timisoara': " + cityBST.depth("Timisoara"));
    
        System.out.println("\nKeys in order:");
        ArrayList<String> orderedKeys = cityBST.getKeysInOrder();
        for (String key : orderedKeys) {
            System.out.println(key);
        }
        
        System.out.println("\nIs BST check: " + cityBST.isBST());
        System.out.println("Is perfectly balanced: " + cityBST.isPerfectlyBalanced());
        
        System.out.println("\nDeleting 'Brasov'...");
        cityBST.delete("Brasov");
        System.out.println("BST after deletion (inorder):");
        cityBST.inorder();
        
        System.out.println("\nIterative operations:");
        System.out.println("SearchIterative for 'Oradea': " + cityBST.searchIterative("Oradea"));
        System.out.println("SearchIterative for 'Bucuresti': " + cityBST.searchIterative("Bucuresti"));
        System.out.println("MinIterative: " + cityBST.minIterative());
        
        System.out.println("\nInserting 'Constanta' iteratively...");
        cityBST.insertIterative("Constanta", new City("Constanta", 220, 310000));
        System.out.println("BST after iterative insertion (inorder):");
        cityBST.inorder();
        
        System.out.println("\nLevel order traversal:");
        cityBST.printLevels();
        
        System.out.println("\nPrintPathFromTo operations:");
        System.out.println("Path from 'Arad' to 'Timisoara':");
        cityBST.printPathFromTo("Arad", "Timisoara");
        System.out.println("Path from 'Cluj' to 'Sibiu':");
        cityBST.printPathFromTo("Cluj", "Sibiu");
        
        GenericBST<Integer, String> intBST = new GenericBST<>();
        intBST.put(50, "Fifty");
        intBST.put(30, "Thirty");
        intBST.put(70, "Seventy");
        intBST.put(20, "Twenty");
        intBST.put(40, "Forty");
        intBST.put(60, "Sixty");
        intBST.put(80, "Eighty");
        
        System.out.println("\n\n==== Testing numeric operations with Integer BST ====");
        System.out.println("Integer BST contents (inorder):");
        intBST.inorder();
        
        System.out.println("\nSearchClosest operations:");
        System.out.println("Closest to 35: " + intBST.searchClosest(35));
        System.out.println("Closest to 53: " + intBST.searchClosest(53));
        System.out.println("Closest to 77: " + intBST.searchClosest(77));
        System.out.println("Closest to 44: " + intBST.searchClosest(44));
        System.out.println("Closest to 26: " + intBST.searchClosest(26));
        
        System.out.println("\nCheckExistTwoNodesWithSum operations:");
        System.out.println("Sum 90 exists: " + intBST.checkExistTwoNodesWithSum(90));
        System.out.println("Sum 100 exists: " + intBST.checkExistTwoNodesWithSum(100));
        System.out.println("Sum 150 exists: " + intBST.checkExistTwoNodesWithSum(150));
        intBST.inorder();
        System.out.println("\nPrintPathsWithSum operations:");
        System.out.println("Paths with sum 100:");
        intBST.printPathsWithSum(100);
        System.out.println("Paths with sum 150:");
        intBST.printPathsWithSum(150);
        System.out.println("Paths with sum 0:");
        intBST.printPathsWithSum(0);
        System.out.println("Paths with sum 15:");
        intBST.printPathsWithSum(15);
        System.out.println("Paths with sum 50:");
        intBST.printPathsWithSum(50);
        System.out.println("Paths with sum 180:");
        intBST.printPathsWithSum(180);
        System.out.println("Paths with sum 120:");
        intBST.printPathsWithSum(120);
        
        System.out.println("\nSpoiling values operations:");
        
        GenericBST<String, String> stringBST = new GenericBST<>();
        stringBST.put("A", "Alpha");
        stringBST.put("B", "Beta");
        stringBST.put("C", "Charlie");
        stringBST.put("D", "Delta");
        stringBST.put("E", "Echo");
        
        System.out.println("\nString BST before spoiling:");
        stringBST.inorder();
        
        System.out.println("\nSpoiling values...");
        stringBST.spoilValues();
        
        System.out.println("\nString BST after spoiling:");
        stringBST.inorder();
        
        System.out.println("\nInteger BST before spoiling:");
        intBST.inorder();
        
        System.out.println("\nSpoiling values...");
        intBST.spoilValues();
        
        System.out.println("\nInteger BST after spoiling:");
        intBST.inorder();
    }
}

class City {
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