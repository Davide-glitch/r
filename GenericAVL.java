import java.util.Queue;

interface GenericAVLInterface<Key extends Comparable<Key>, Value> {
    void put(Key key, Value val);
    Value get(Key key);
    void remove(Key key);
    boolean containsKey(Key key);
    int height();
    void displayLevels();
    Iterable<Key> getKeys();
    Key firstKey();
    Key lastKey();
    void rotateLeft(Key key);
    void rotateRight(Key key);
}

public class GenericAVL<Key extends Comparable<Key>, Value> implements GenericAVLInterface<Key, Value> {

    private class AVLNode {
        Key key;
        Value val;
        AVLNode left;
        AVLNode right;
        int height;

        public AVLNode(Key key, Value val, int height) {
            this.key = key;
            this.val = val;
            this.height = height;
            this.left = null;
            this.right = null;
        }
    }

    private AVLNode root;

    public GenericAVL() {
        root = null;
    }

    @Override
    public int height() {
        return height(root);
    }

    private int height(AVLNode x) {
        if (x == null)
            return -1;
        return x.height;
    }

    @Override
    public boolean containsKey(Key key) {
        return get(key) != null;
    }

    @Override
    public Value get(Key key) {
        if (key == null)
            throw new IllegalArgumentException("key is null");
        AVLNode x = get(root, key);
        if (x == null)
            return null;
        return x.val;
    }

    private AVLNode get(AVLNode x, Key key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return get(x.left, key);
        else if (cmp > 0)
            return get(x.right, key);
        else
            return x;
    }

    @Override
    public void put(Key key, Value val) {
        if (key == null)
            throw new IllegalArgumentException("key is null");
        root = put(root, key, val);
    }

    private AVLNode put(AVLNode x, Key key, Value val) {
        if (x == null)
            return new AVLNode(key, val, 0);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
            return x;
        }
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }

    private AVLNode balance(AVLNode x) {
        if (balanceFactor(x) < -1) {
            if (balanceFactor(x.right) > 0) {
                x.right = rotateRightNode(x.right);
            }
            x = rotateLeftNode(x);
        } else if (balanceFactor(x) > 1) {
            if (balanceFactor(x.left) < 0) {
                x.left = rotateLeftNode(x.left);
            }
            x = rotateRightNode(x);
        }
        return x;
    }

    private int balanceFactor(AVLNode x) {
        return height(x.left) - height(x.right);
    }

    @Override
    public void rotateRight(Key key) {
        root = rotateRight(root, key);
    }

    private AVLNode rotateRight(AVLNode x, Key key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = rotateRight(x.left, key);
        } else if (cmp > 0) {
            x.right = rotateRight(x.right, key);
        } else {
            x = rotateRightNode(x);
        }
        return x;
    }

    private AVLNode rotateRightNode(AVLNode y) {
        System.out.println("Rotate right at " + y.key);
        AVLNode x = y.left;
        y.left = x.right;
        x.right = y;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    @Override
    public void rotateLeft(Key key) {
        root = rotateLeft(root, key);
    }

    private AVLNode rotateLeft(AVLNode x, Key key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = rotateLeft(x.left, key);
        } else if (cmp > 0) {
            x.right = rotateLeft(x.right, key);
        } else {
            x = rotateLeftNode(x);
        }
        return x;
    }

    private AVLNode rotateLeftNode(AVLNode x) {
        System.out.println("Rotate left at " + x.key);
        AVLNode y = x.right;
        x.right = y.left;
        y.left = x;
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }

    @Override
    public void remove(Key key) {
        if (key == null)
            throw new IllegalArgumentException("key is null");
        root = remove(root, key);
    }

    private AVLNode remove(AVLNode x, Key key) {
        if (x == null)
            return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(x.left, key);
        } else if (cmp > 0) {
            x.right = remove(x.right, key);
        } else {
            if (x.left == null)
                return x.right;
            if (x.right == null)
                return x.left;

            AVLNode t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }

        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }

    private AVLNode min(AVLNode x) {
        if (x.left == null)
            return x;
        return min(x.left);
    }

    private AVLNode deleteMin(AVLNode x) {
        if (x.left == null)
            return x.right;
        x.left = deleteMin(x.left);
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return balance(x);
    }

    @Override
    public Key firstKey() {
        if (root == null)
            return null;
        return min(root).key;
    }

    @Override
    public Key lastKey() {
        if (root == null)
            return null;
        return max(root).key;
    }

    private AVLNode max(AVLNode x) {
        if (x.right == null)
            return x;
        return max(x.right);
    }

    @Override
    public Iterable<Key> getKeys() {
        Queue<Key> q = new java.util.LinkedList<Key>();
        inorder(root, q);
        return q;
    }


    private void inorder(AVLNode x, Queue<Key> q) {
        if (x == null)
            return;
        inorder(x.left, q);
        q.add(x.key);
        inorder(x.right, q);
    }

    @Override
    public void displayLevels() {
        Queue<QueuePair> q = new java.util.LinkedList<QueuePair>();

        System.out.println("AVL Tree displayed on levels: ");
        AVLNode x = root;
        int oldLevel = -1;
        int level = 0;

        if (root != null)
            q.add(new QueuePair(x, level));

        while (!q.isEmpty()) {
            QueuePair p = q.remove();
            x = p.node;
            level = p.level;

            if (level > oldLevel) {
                System.out.println();
                System.out.print("Level " + level + ":  ");
                oldLevel = level;
            }

            System.out.print(x.key + " ");
            if (x.left != null)
                q.add(new QueuePair(x.left, level + 1));
            if (x.right != null)
                q.add(new QueuePair(x.right, level + 1));      
        }
        System.out.println();
        System.out.println();
    }

    private class QueuePair {
        AVLNode node;
        int level;

        QueuePair(AVLNode node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    public static void main(String[] args) {
        GenericAVL<Integer, String> intTree = new GenericAVL<>();

        System.out.println("Testing GenericAVL with Integer keys and String values:");
        Integer[] numbers = { 8, 9, 10, 11, 15, 3, 17, 22, 25, 16 };

        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Insert " + numbers[i] + " -> Value " + i);
            intTree.put(numbers[i], "Value" + i);
            intTree.displayLevels();
        }

        System.out.println("\nTesting GenericAVL with String keys:");
        GenericAVL<String, Integer> strTree = new GenericAVL<>();

        String[] names = { "David", "Alice", "Bob", "Charlie", "Eva", "Frank", "Grace" };

        for (int i = 0; i < names.length; i++) {
            System.out.println("Insert " + names[i] + " -> " + i);
            strTree.put(names[i], i);
            strTree.displayLevels();
        }

        System.out.println("\nTesting rotations on the Integer tree:");
        intTree.rotateLeft(15);
        intTree.displayLevels();
        intTree.rotateRight(17);
        intTree.displayLevels();

        System.out.println("\nTesting first and last key:");
        System.out.println("First key: " + intTree.firstKey());
        System.out.println("Last key: " + intTree.lastKey());
    }
}