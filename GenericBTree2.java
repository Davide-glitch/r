import java.util.LinkedList;
import java.util.Queue;

interface Btree {
    void constructTreeFromFigure();
    void displayLevels();
}

public class GenericBTree2<K extends Comparable<K>> implements Btree {

    private final int T;

    class BTreeNode {
        int n;
        K[] key;
        BTreeNode[] child;
        boolean leaf = true;      

        @SuppressWarnings("unchecked")
        public BTreeNode() {
            this.key = (K[]) new Comparable[2 * T - 1];
            this.child = (BTreeNode[]) new GenericBTree2.BTreeNode[2 * T];
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
                sb.append(key[i]);
            }
            sb.append(" ] ");
            return sb.toString();
        }
    }

    private BTreeNode root;

    public GenericBTree2(int t) {
        T = t;
        root = null;
    }

    @SuppressWarnings("unchecked")
    public void constructTreeFromFigure() {
        // Example using Integer values that can be replaced with any comparable type
        root = new BTreeNode();
        root.n = 5;
        root.leaf = false;
        root.key[0] = (K) Integer.valueOf(5);
        root.key[1] = (K) Integer.valueOf(12);
        root.key[2] = (K) Integer.valueOf(16);
        root.key[3] = (K) Integer.valueOf(23);
        root.key[4] = (K) Integer.valueOf(30);

        BTreeNode child1 = new BTreeNode();
        child1.n = 3;
        child1.leaf = true;
        child1.key[0] = (K) Integer.valueOf(1);
        child1.key[1] = (K) Integer.valueOf(2);
        child1.key[2] = (K) Integer.valueOf(3);

        BTreeNode child2 = new BTreeNode();
        child2.n = 5;
        child2.leaf = true;
        child2.key[0] = (K) Integer.valueOf(6);
        child2.key[1] = (K) Integer.valueOf(7);
        child2.key[2] = (K) Integer.valueOf(9);
        child2.key[3] = (K) Integer.valueOf(10);
        child2.key[4] = (K) Integer.valueOf(11);

        BTreeNode child3 = new BTreeNode();
        child3.n = 2;
        child3.leaf = true;
        child3.key[0] = (K) Integer.valueOf(13);
        child3.key[1] = (K) Integer.valueOf(14);

        BTreeNode child4 = new BTreeNode();
        child4.n = 3;
        child4.leaf = true;
        child4.key[0] = (K) Integer.valueOf(18);
        child4.key[1] = (K) Integer.valueOf(19);
        child4.key[2] = (K) Integer.valueOf(20);

        BTreeNode child5 = new BTreeNode();
        child5.n = 2;
        child5.leaf = true;
        child5.key[0] = (K) Integer.valueOf(24);
        child5.key[1] = (K) Integer.valueOf(26);

        BTreeNode child6 = new BTreeNode();
        child6.n = 2;
        child6.leaf = true;
        child6.key[0] = (K) Integer.valueOf(32);
        child6.key[1] = (K) Integer.valueOf(36);

        root.child[0] = child1;
        root.child[1] = child2;
        root.child[2] = child3;
        root.child[3] = child4;
        root.child[4] = child5;
        root.child[5] = child6;
    }

    private class QueuePair {
        BTreeNode node;
        int level;

        QueuePair(BTreeNode node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    public void displayLevels() {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }

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
        GenericBTree2<Integer> intTree = new GenericBTree2<>(3);
        intTree.constructTreeFromFigure();
        System.out.println("Constructed Integer B-Tree of mindegree t=3:");
        intTree.displayLevels();
    }
}