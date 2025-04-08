import java.util.LinkedList;
import java.util.Queue;

interface GenericBTree1Interface<K extends Comparable<K>> {
    void insert(K key);
    boolean contains(K key);
    int height();
    int level(K key);
    K min();
    K max();
    K successor(K key);
    K predecessor(K key);
    void inorder();
    void displayLevels();
}

public class GenericBTree1<K extends Comparable<K>> implements GenericBTree1Interface<K> {

    private int T;

    class BTreeNode {
        int n;
        K[] keys;
        BTreeNode[] child;
        boolean leaf = true;

        @SuppressWarnings("unchecked")
        public BTreeNode() {
            this.keys = (K[]) new Comparable[2 * T - 1];
            this.child = (BTreeNode[]) new GenericBTree1.BTreeNode[2 * T];
            this.n = 0;
            this.leaf = true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(" [ ");
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    sb.append(" "); 
                }
                sb.append(keys[i]);
            }
            sb.append(" ] ");
            return sb.toString();
        }
    }

    private BTreeNode root;

    public GenericBTree1(int t) {
        if (t < 2) {
            throw new IllegalArgumentException("B-Tree degree must be at least 2");
        }
        this.T = t;
        this.root = new BTreeNode();
    }

    @Override
    public void insert(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        BTreeNode r = root;
        if (r.n == 2 * T - 1) {
            BTreeNode s = new BTreeNode();
            root = s;
            s.leaf = false;
            s.n = 0;
            s.child[0] = r;
            split(s, 0, r);
            insertNonfull(s, key);
        } else {
            insertNonfull(r, key);
        }
    }

    private void split(BTreeNode x, int pos, BTreeNode y) {
        BTreeNode z = new BTreeNode();
        z.leaf = y.leaf;
        z.n = T - 1;

        for (int j = 0; j < T - 1; j++) {
            z.keys[j] = y.keys[j + T];
        }

        if (!y.leaf) {
            for (int j = 0; j < T; j++) {
                z.child[j] = y.child[j + T];
            }
        }

        y.n = T - 1;

        for (int j = x.n; j >= pos + 1; j--) {
            x.child[j + 1] = x.child[j];
        }

        x.child[pos + 1] = z;

        for (int j = x.n - 1; j >= pos; j--) {
            x.keys[j + 1] = x.keys[j];
        }

        x.keys[pos] = y.keys[T - 1];
        x.n = x.n + 1;
    }

    private void insertNonfull(BTreeNode x, K k) {
        if (x.leaf) {
            int i = x.n - 1;

            while (i >= 0 && k.compareTo(x.keys[i]) < 0) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }

            x.keys[i + 1] = k;
            x.n = x.n + 1;
        } else {
            int i = x.n - 1;

            while (i >= 0 && k.compareTo(x.keys[i]) < 0) {
                i--;
            }
            i++;

            BTreeNode tmp = x.child[i];
            if (tmp.n == 2 * T - 1) {
                split(x, i, tmp);

                if (k.compareTo(x.keys[i]) > 0) {
                    i++;
                }
            }

            insertNonfull(x.child[i], k);
        }
    }

    @Override
    public boolean contains(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return containsHelper(root, key);
    }

    private boolean containsHelper(BTreeNode node, K key) {
        int i = 0;
        while (i < node.n && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.n && key.compareTo(node.keys[i]) == 0) {
            return true;
        }

        if (node.leaf) {
            return false;
        }

        return containsHelper(node.child[i], key);
    }

    @Override
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(BTreeNode node) {
        if (node == null) {
            return -1;
        }

        if (node.leaf) {
            return 0;
        }

        return 1 + heightHelper(node.child[0]);
    }

    @Override
    public int level(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        return levelHelper(root, key, 0);
    }

    private int levelHelper(BTreeNode node, K key, int level) {
        if (node == null) {
            return -1;
        }

        int i = 0;
        while (i < node.n && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.n && key.compareTo(node.keys[i]) == 0) {
            return level;
        }

        if (node.leaf) {
            return -1;
        }

        return levelHelper(node.child[i], key, level + 1);
    }

    @Override
    public K min() {
        if (root.n == 0) {
            return null;
        }

        return findMin(root);
    }

    private K findMin(BTreeNode node) {
        while (!node.leaf) {
            node = node.child[0];
        }

        return node.keys[0];
    }

    @Override
    public K max() {
        if (root.n == 0) {
            return null;
        }

        return findMax(root);
    }

    private K findMax(BTreeNode node) {
        while (!node.leaf) {
            node = node.child[node.n];
        }

        return node.keys[node.n - 1];
    }

    @Override
    public K successor(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return findSuccessor(root, key, null);
    }

    private K findSuccessor(BTreeNode node, K key, K successor) {
        if (node == null) {
            return successor;
        }

        int i = 0;
        while (i < node.n && key.compareTo(node.keys[i]) >= 0) {
            i++;
        }

        if (i < node.n) {
            if (successor == null || node.keys[i].compareTo(successor) < 0) {
                successor = node.keys[i];
            }
        }

        if (node.leaf) {
            return successor;
        }

        if (i > 0 && key.compareTo(node.keys[i - 1]) == 0) {
            K minInRightSubtree = findMin(node.child[i]);
            if (successor == null || minInRightSubtree.compareTo(successor) < 0) {
                successor = minInRightSubtree;
            }
            return successor;
        }

        return findSuccessor(node.child[i], key, successor);
    }

    @Override
    public K predecessor(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return findPredecessor(root, key, null);
    }

    private K findPredecessor(BTreeNode node, K key, K predecessor) {
        if (node == null) {
            return predecessor;
        }

        int i = 0;
        while (i < node.n && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i > 0) {
            if (predecessor == null || node.keys[i - 1].compareTo(predecessor) > 0) {
                predecessor = node.keys[i - 1];
            }
        }

        if (node.leaf) {
            return predecessor;
        }

        if (i < node.n && key.compareTo(node.keys[i]) == 0) {
            K maxInLeftSubtree = findMax(node.child[i]);
            if (predecessor == null || maxInLeftSubtree.compareTo(predecessor) > 0) {
                predecessor = maxInLeftSubtree;
            }
            return predecessor;
        }

        return findPredecessor(node.child[i], key, predecessor);
    }

    @Override
    public void inorder() {
        System.out.print("Inorder traversal: ");
        inorderHelper(root);
        System.out.println();
    }

    private void inorderHelper(BTreeNode node) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < node.n; i++) {
            if (!node.leaf) {
                inorderHelper(node.child[i]);
            }

            System.out.print(node.keys[i] + " ");
        }

        if (!node.leaf) {
            inorderHelper(node.child[node.n]);
        }
    }

    private class QueuePair {
        BTreeNode node;
        int level;

        QueuePair(BTreeNode node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    @Override
    public void displayLevels() {
        Queue<QueuePair> q = new LinkedList<>();

        System.out.println("B Tree displayed on levels: ");
        BTreeNode x = root;
        int oldLevel = 0;
        int level;

        q.add(new QueuePair(x, oldLevel));

        while (!q.isEmpty()) {
            QueuePair p = q.remove();
            x = p.node;
            level = p.level;

            if (level > oldLevel) {
                System.out.println();
                oldLevel = level;
            }

            System.out.print(x.toString());

            if (!x.leaf) {
                for (int i = 0; i <= x.n; i++) {
                    BTreeNode y = (x.child)[i];
                    q.add(new QueuePair(y, level + 1));
                }
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        GenericBTree1<Integer> b = new GenericBTree1<>(3);

        int[] a = {8, 9, 10, 11, 15, 20, 17, 22, 25, 16, 12, 13, 14, 26, 27, 29, 33, 40, 50, 51, 52, 53};

        for (int i = 0; i < a.length; i++) {
            System.out.println("Will insert " + a[i]);
            b.insert(a[i]);
            b.displayLevels();
        }

        System.out.println("\nHeight of the tree: " + b.height());
        System.out.println("Minimum key: " + b.min());
        System.out.println("Maximum key: " + b.max());

        int testKey = 20;
        System.out.println("Contains " + testKey + ": " + b.contains(testKey));
        System.out.println("Level of " + testKey + ": " + b.level(testKey));

        System.out.println("Successor of " + testKey + ": " + b.successor(testKey));
        System.out.println("Predecessor of " + testKey + ": " + b.predecessor(testKey));

        b.inorder();

        System.out.println("\nCreating a B-Tree with String keys:");
        GenericBTree1<String> stringTree = new GenericBTree1<>(2);
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};

        for (String word : words) {
            System.out.println("Inserting: " + word);
            stringTree.insert(word);
            stringTree.displayLevels();
        }

        stringTree.inorder();
    }
}    